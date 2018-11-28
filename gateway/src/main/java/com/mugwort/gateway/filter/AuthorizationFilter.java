package com.mugwort.gateway.filter;

import com.hand.hospital.common.api.ApiResponseBuilder;
import com.hand.hospital.common.bean.login.*;
import com.hand.hospital.common.constant.ErrorCodeEnum;
import com.hand.hospital.common.constant.FilterOrderConstant;
import com.hand.hospital.common.constant.HeaderConstant;
import com.hand.hospital.common.exception.BusinessException;
import com.hand.hospital.common.number.NumberUtil;
import com.hand.hospital.common.scan.log2mq.gateway.GatewayLog2MQAsyncProcessor;
import com.hand.hospital.common.security.CryptoUtil;
import com.hand.hospital.common.util.*;
import com.mugwort.gateway.config.IgnoreConfig;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.hand.hospital.common.constant.HeaderConstant.*;
import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

/**
 * 参数防篡改
 */
@Component
@Slf4j
public class AuthorizationFilter extends ZuulFilter {

    @Autowired
    private IgnoreConfig ignoreConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private GatewayLog2MQAsyncProcessor mqLogAsyncProcessor;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return FilterOrderConstant.AUTHORIZATION_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = getCurrentContext();
        HttpServletRequest request = context.getRequest();
        try {
            // 设置UUID
            String uuid = UUID.randomUUID().toString().replace("-", "");
            context.addZuulRequestHeader(HeaderConstant.HEADER_X_WEIMAI_UUID_LOWER, uuid);
            context.addZuulResponseHeader(HeaderConstant.HEADER_X_WEIMAI_UUID_LOWER, uuid);
            context.addZuulResponseHeader(HeaderConstant.HEADER_X_WEIMAI_START_TIME_LOWER, String.valueOf(System.currentTimeMillis()));

            // 开始验证请求参数
            InputStream in = (InputStream) context.get("requestEntity");
            if (in == null) {
                in = context.getRequest().getInputStream();
            }
            String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
            final byte[] bytes = body.getBytes("UTF-8");

            // 验证头字段是否合法
            boolean isHeaderLegal = validateOfHeaders(request);
            if (!isHeaderLegal && StringUtil.isNotEmpty(request.getHeader(HEADER_X_WEIMAI_ORIGIN))) { // 仅对掌医体系的请求做验证
                log.error("header field is legal!");
                //过滤该请求，不往下级服务去转发请求，到此结束
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(401);
                context.setResponseBody(JsonUtil.toJson(ApiResponseBuilder.buildResponse(ErrorCodeEnum.HEADER_ILLEGAL)));
                return null;
            }

            // 掌医中每个客户端都会隶属于某个集团，集团id位于Authorization字段的第二个值
            String authField = request.getHeader(HeaderConstant.HEADER_AUTHORIZATION);
            if (StringUtil.isNotEmpty(authField)) {
                String[] authFields = authField.split(":");
                if (null != authFields && authFields.length > 1) {
                    context.addZuulRequestHeader(HeaderConstant.HEADER_AUTHORIZATION_GRP_ID, authFields[1]);
                }
            }

            // 检查是否需要进行加密验签比对
            if (isNeedAuthorization(request)) {

                // 验证时间戳是否过期
                boolean isExpired = validateOfDate(request);
                if (!isExpired) {
                    log.error("date field is expired!");
                    context.setSendZuulResponse(false);
                    context.setResponseStatusCode(402);
                    context.setResponseBody(JsonUtil.toJson(ApiResponseBuilder.buildResponse(ErrorCodeEnum.HEADER_DATE_EXPIRED)));
                    return null;
                }

                // 验证加密结果是否正确
                boolean isCorrectSign = validateOfAuthorization(request, body, uuid);
                if (!isCorrectSign) {
                    log.error("加密结果不一致!");
                    context.setSendZuulResponse(false);
                    context.setResponseStatusCode(403);
                    context.setResponseBody(JsonUtil.toJson(ApiResponseBuilder.buildResponse(ErrorCodeEnum.PARAM_ENC_ILLEGAL)));
                    return null;
                }

                // 最后
                context.setRequest(new HttpServletRequestWrapper(getCurrentContext().getRequest()) {
                    @Override
                    public ServletInputStream getInputStream() throws IOException {
                        return new ServletInputStreamWrapper(bytes);
                    }

                    @Override
                    public int getContentLength() {
                        return bytes.length;
                    }

                    @Override
                    public long getContentLengthLong() {
                        return bytes.length;
                    }
                });
            }

        } catch (Exception e) {
            if (e instanceof BusinessException) {
                BusinessException be = (BusinessException) e;
                throw new ZuulException(be, be.getMessage(), be.getCode(), be.getMessage());
            } else {
                rethrowRuntimeException(e);
            }
        }

        return null;
    }

    /**
     * 检查接口是否需要进行加密验签比对
     *
     * @param request
     * @return true：需要进行验签，false：不需要验签
     */
    private boolean isNeedAuthorization(HttpServletRequest request) {
        String url = request.getRequestURI();
        String authorization = request.getHeader("Authorization");
        List<String> ignoreUrls = ignoreConfig.items();
        boolean flag = true;
        if (null != url && null != ignoreUrls) {
            for (String ignoreUrl : ignoreUrls) {
                if (url.contains(ignoreUrl.trim())) {
                    flag = false;
                    break;
                }
            }
        }
        log.debug("Determine whether to encrypt uri, url = {}, authorization = {}, ignoreUrls = {}, flag = {}", url, authorization, ignoreUrls, flag);
        return flag;
    }

    /**
     * 检查header中要求的属性是否存在或合法，true：验证通过，false：验证不通过
     *
     * @param request
     * @return
     */
    private boolean validateOfHeaders(HttpServletRequest request) {
        if (jumpSwagger(request)) {
            return true;
        }
        String authorization = request.getHeader(HEADER_AUTHORIZATION);
        String origin = request.getHeader(HEADER_X_WEIMAI_ORIGIN);
        String date = request.getHeader(HEADER_X_WEIMAI_DATE);
        String version = request.getHeader(HEADER_X_WEIMAI_VERSION);

        // 头部参数校验
        log.info("validate header authorization = {}, date = {}, version = {}, origin = {}", authorization, date, version, origin);
        if (StringUtil.isEmpty(authorization) || StringUtil.isEmpty(date)
                || StringUtil.isEmpty(version) || StringUtil.isEmpty(origin)) {
            return false;
        }
        if (!NumberUtil.isNumeric(date)) {
            return false;
        }

        String[] authFields = authorization.split(":");
        if (null == authFields || authFields.length != 3) {
            return false;
        }

        return true;
    }

    /**
     * 验证时间戳是否过期, true: 验证通过-未过期，false: 验证不通过-已过期
     *
     * @param request
     * @return
     */
    private boolean validateOfDate(HttpServletRequest request) {
        if (jumpSwagger(request)) {
            return true;
        }
        // 时间校验
        String headerDate = request.getHeader(HEADER_X_WEIMAI_DATE);
        if (DateUtil.isExpired(Long.valueOf(headerDate), HeaderConstant.AUTH_EXPIRE_TIME)) {
            log.error("Client header date is: {}, then server date is: {}, but server allow interval one day", headerDate, System.currentTimeMillis());
            return false;
        }
        return true;
    }

    /**
     * 验证加密结果是否一致：是否被篡改
     *
     * @param request
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private boolean validateOfAuthorization(HttpServletRequest request, String body, String uuid) throws IOException, BusinessException {
        if (jumpSwagger(request)) {
            return true;
        }
        String authorization = request.getHeader(HEADER_AUTHORIZATION);
        if (log.isDebugEnabled()) {
            log.debug("Authorization header is: {}", authorization);
        }
        if (null == authorization || !authorization.contains(":")) {
            log.error("Authorization is empty or not correct");
            return false;
        }

        String[] authFieldValue = authorization.split(":");
        if (null != authFieldValue && authFieldValue.length > 0) {
            String authorizationSchema = authFieldValue[0];
            if (authorizationSchema.equals(AUTH_SCHEMA_WEIMAI)) {
                // 对所有参数进行字典升序排列,并返回排序好的字符串
                String content = dictSort(request, body);
                // 对参数内容进行加密签名
                log.info("Signature before content is: {}", content);

                String defaultSecret = CryptoUtil.md5(env.getProperty("user.defaultSecret")); // 默认的秘钥
                String actualSecret = defaultSecret;
                String token = request.getHeader(HeaderConstant.HEADER_X_WEIMAI_TOKEN);
                if (!StringUtil.isEmpty(token)) {
                    String globalId = JwtUtil.getUid(token);
                    String loginUserKeyTpl = judgeLoginUserRedisKeyTpl(request);
                    String userCacheKey = String.format(loginUserKeyTpl, globalId);
                    String loginUserJson = RedisUtil.get(redisTemplate, userCacheKey);
                    if (!StringUtil.isEmpty(loginUserJson)) {
                        LoginUser loginUser = JsonUtil.toObject(loginUserJson, LoginUser.class);
                        if (null != loginUser) {
                            actualSecret = loginUser.getSecret();
                            // 异步记录登录用户信息
                            mqLogAsyncProcessor.logUser(uuid, loginUser, request.getMethod(), DateUtil.getCurrentDate());
                        }
                    }
                }

                String signature = CryptoUtil.hmacSha1Str(content.getBytes(), actualSecret.getBytes());
                if (StringUtils.isEmpty(signature)) {
                    log.error("Signature is empty, service occur exception");
                    return false;
                }

                log.info("default secret = {}, actual secret = {}, token = {}, client signature is: {}, server signature is: {}", defaultSecret, actualSecret, token, authFieldValue[2], signature);
                // 对加密结果与头部字段进行比较
                if (null != signature && signature.equals(authFieldValue[2])) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            log.error("Client authorization filed is incorrect!");
            return false;
        }

        return false;
    }

    /**
     * 对请求的header和参数进行字典排序(升序),并生成一个字符串
     *
     * @param request
     * @return
     */
    private String dictSort(HttpServletRequest request, String body) throws IOException {
        TreeMap<String, String> treeMap = new TreeMap<String, String>();

        // 判空处理
        String origin = StringUtil.isEmpty(request.getHeader(HEADER_X_WEIMAI_ORIGIN)) ? "" : request.getHeader(HEADER_X_WEIMAI_ORIGIN);
        String other = StringUtil.isEmpty(request.getHeader(HEADER_X_WEIMAI_OTHER)) ? "" : request.getHeader(HEADER_X_WEIMAI_OTHER);
        String date = StringUtil.isEmpty(request.getHeader(HEADER_X_WEIMAI_DATE)) ? "" : request.getHeader(HEADER_X_WEIMAI_DATE);
        String token = StringUtil.isEmpty(request.getHeader(HEADER_X_WEIMAI_TOKEN)) ? "" : request.getHeader(HEADER_X_WEIMAI_TOKEN);
        String version = StringUtil.isEmpty(request.getHeader(HEADER_X_WEIMAI_VERSION)) ? "" : request.getHeader(HEADER_X_WEIMAI_VERSION);

        // 排序
        treeMap.put(HEADER_X_WEIMAI_ORIGIN, origin);
        treeMap.put(HEADER_X_WEIMAI_OTHER, other);
        treeMap.put(HEADER_X_WEIMAI_DATE, date);
        treeMap.put(HEADER_X_WEIMAI_TOKEN, token);
        treeMap.put(HEADER_X_WEIMAI_VERSION, version);

        // 请求路径
        String url = request.getRequestURI();
        if (!StringUtils.isEmpty(request.getQueryString())) {
            if (url.length() > 1 && url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            url += "?" + request.getQueryString();
        }
        url = URLDecoder.decode(url, "UTF-8");
        treeMap.put(HEADER_X_WEIMAI_URL, url);

        // 获取body内容
        if (!StringUtils.isEmpty(body) && !body.equals("{}")) {
            treeMap.put(HEADER_X_WEIMAI_BODY, StringUtil.replaceBlank(body));
        }

        // 拼接所有请求参数字符串
        Set<Map.Entry<String, String>> set = treeMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();
        StringBuffer strBuffer = new StringBuffer();
        while (iterator.hasNext()) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iterator.next();
            strBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String content = strBuffer.toString();

        return content.substring(0, content.length() - 1);
    }

    /**
     * 描述: 跳过swagger不做验证
     * 参数: [request]
     * 返回:
     * 作者: 白前
     * 日期: 2018/7/2
     */
    private boolean jumpSwagger(HttpServletRequest request) {
        String authorization = request.getHeader(HEADER_AUTHORIZATION);
        if (null != authorization && authorization.contains(HeaderConstant.AUTH_SCHEMA_SWAGGER)) {
            return true;
        }
        return false;
    }

    /**
     * 描述: 判断是哪个终端用户，返回对应的user login key；
     * 作者: 白前
     * 日期: 2018/6/29
     */
    private String judgeLoginUserRedisKeyTpl(HttpServletRequest request) {
        String loginUserKeyTpl = "";
        String origin = request.getHeader(HeaderConstant.HEADER_X_WEIMAI_ORIGIN);
        if (origin.contains(HeaderConstant.ORIGIN_APP_PATIENT)) {
            loginUserKeyTpl = AppUser.CACHE_KEY_TPL;
        } else if (origin.contains(HeaderConstant.ORIGIN_PC_PMS)) {
            loginUserKeyTpl = PmsUser.CACHE_KEY_TPL;
        } else if (origin.contains(HeaderConstant.ORIGIN_PC_OMS)) {
            loginUserKeyTpl = OmsUser.CACHE_KEY_TPL;
        } else if (origin.contains(HeaderConstant.ORIGIN_BROWSER_H5)) {
            loginUserKeyTpl = WeChatUser.CACHE_KEY_TPL;
        }
        return loginUserKeyTpl;
    }
}

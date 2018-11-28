package com.mugwort.gateway.filter;

import com.hand.hospital.common.constant.FilterOrderConstant;
import com.hand.hospital.common.constant.HeaderConstant;
import com.hand.hospital.common.scan.log2mq.gateway.GatewayLog2MQAsyncProcessor;
import com.hand.hospital.common.util.DateUtil;
import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

@Component
@Slf4j
public class ResponseBodyFilter extends ZuulFilter {
    @Autowired
    private GatewayLog2MQAsyncProcessor mqLogAsyncProcessor;

    @Override
    public String filterType() {
        return "post";
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
        try {
            RequestContext context = getCurrentContext();
            InputStream stream = context.getResponseDataStream();
            final String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            log.debug("response body is:\n\r {} ", body);

            List<Pair<String, String>> headers = context.getZuulResponseHeaders();
            String uuid = null;
            long startTime = 0;
            for (Pair pair : headers) {
                String key = (String) pair.first();
                if (null != key && HeaderConstant.HEADER_X_WEIMAI_UUID_LOWER.equals(key.toLowerCase())) {
                    uuid = String.valueOf(pair.second());
                }
                if (null != key && HeaderConstant.HEADER_X_WEIMAI_START_TIME_LOWER.equals(key.toLowerCase())) {
                    startTime = Long.parseLong(pair.second().toString());
                }
            }
            long costTime = System.currentTimeMillis() - startTime;
            HttpServletRequest request = context.getRequest();
            String uri = request.getRequestURI();
            log.info("uri = {}, uuid = {}, cost time = {}ms", uri, uuid, costTime);

            context.setResponseBody(body);

            // 异步记录响应数据
            mqLogAsyncProcessor.logResponse(uri, uuid, costTime, body, request.getMethod(), DateUtil.getCurrentDate());

        } catch (Exception e) {
            log.error("response body filter occur exception: {}", e);
            rethrowRuntimeException(e);
        }
        return null;
    }
}

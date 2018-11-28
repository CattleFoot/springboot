package com.mugwort.gateway.filter;

import com.hand.hospital.common.constant.FilterOrderConstant;
import com.hand.hospital.common.constant.HeaderConstant;
import com.hand.hospital.common.scan.log2mq.gateway.GatewayLog2MQAsyncProcessor;
import com.hand.hospital.common.util.DateUtil;
import com.hand.hospital.common.util.StringUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

/**
 * 请求参数拦截，并记录到日志系统
 */
@Component
@Slf4j
public class RequestParamFilter extends ZuulFilter {

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
        try {
            RequestContext context = getCurrentContext();
            HttpServletRequest request = context.getRequest();
            log.info("request uri = {}", request.getRequestURI());

            if (StringUtil.isNotEmpty(request.getContentType()) && request.getContentType().contains("application/json")) {
                final String urlParam = request.getQueryString();
                log.debug("request param is: {}", urlParam);

                InputStream in = (InputStream) context.get("requestEntity");
                if (in == null) {
                    in = request.getInputStream();
                }

                final String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
                log.debug("request body is: {}", body);

                final byte[] bytes = body.getBytes("UTF-8");
                HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
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
                };
                context.setRequest(wrapper);
                //context.addZuulResponseHeader("Cache-Control", "max-age=0, must-revalidate");

                // 异步记录请求日志
                String uuid = context.getZuulRequestHeaders() != null ? context.getZuulRequestHeaders().get(HeaderConstant.HEADER_X_WEIMAI_UUID_LOWER) : "";
                mqLogAsyncProcessor.logRequest(request.getRequestURI(), urlParam, body, uuid, request.getMethod(), DateUtil.getCurrentDate());

            }
        } catch (Exception e) {
            log.error("request param filter occur exception: {}", e);
            rethrowRuntimeException(e);
        }
        return null;
    }
}

package com.example.v2exclone.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求日志拦截器
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 记录请求开始时间
        request.setAttribute(START_TIME, System.currentTimeMillis());
        
        // 记录请求信息
        logRequestInfo(request);
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                          Object handler, ModelAndView modelAndView) {
        // 可以在这里记录一些中间处理信息
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) {
        // 记录响应信息
        logResponseInfo(request, response, ex);
    }

    /**
     * 记录请求信息
     */
    private void logRequestInfo(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String remoteAddr = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        // 构建请求URL
        String fullUrl = uri;
        if (queryString != null && !queryString.isEmpty()) {
            fullUrl += "?" + queryString;
        }
        
        log.info("🚀 请求开始 - {} {} from {} - User-Agent: {}", 
                method, fullUrl, remoteAddr, userAgent);
        
        // 记录请求头（可选，调试时开启）
        if (log.isDebugEnabled()) {
            logRequestHeaders(request);
        }
    }

    /**
     * 记录响应信息
     */
    private void logResponseInfo(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME);
        long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;
        
        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();
        
        if (ex != null) {
            log.error("❌ 请求异常 - {} {} - 状态码: {} - 耗时: {}ms - 异常: {}", 
                    method, uri, status, duration, ex.getMessage());
        } else {
            String statusEmoji = getStatusEmoji(status);
            log.info("{} 请求完成 - {} {} - 状态码: {} - 耗时: {}ms", 
                    statusEmoji, method, uri, status, duration);
        }
    }

    /**
     * 记录请求头
     */
    private void logRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            
            // 过滤敏感信息
            if (headerName.toLowerCase().contains("authorization") || 
                headerName.toLowerCase().contains("cookie")) {
                headerValue = "***";
            }
            
            headers.put(headerName, headerValue);
        }
        
        log.debug("📋 请求头: {}", headers);
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    /**
     * 根据状态码获取对应的emoji
     */
    private String getStatusEmoji(int status) {
        if (status >= 200 && status < 300) {
            return "✅";
        } else if (status >= 300 && status < 400) {
            return "🔄";
        } else if (status >= 400 && status < 500) {
            return "⚠️";
        } else {
            return "❌";
        }
    }
}

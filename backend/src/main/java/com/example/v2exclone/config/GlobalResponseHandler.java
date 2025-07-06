package com.example.v2exclone.config;

import com.example.v2exclone.common.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应处理器
 * 统一包装API响应格式
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.example.v2exclone.controller")
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 只处理API控制器的响应
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                 ServerHttpRequest request, ServerHttpResponse response) {
        
        String path = request.getURI().getPath();
        
        // 如果已经是ApiResponse格式，直接返回
        if (body instanceof ApiResponse) {
            ApiResponse<?> apiResponse = (ApiResponse<?>) body;
            if (apiResponse.getPath() == null) {
                apiResponse.setPath(path);
            }
            return body;
        }
        
        // 对于String类型的响应，需要特殊处理
        if (body instanceof String) {
            try {
                ApiResponse<String> stringResponse = ApiResponse.success((String) body);
                stringResponse.setPath(path);
                return objectMapper.writeValueAsString(stringResponse);
            } catch (JsonProcessingException e) {
                log.error("序列化响应失败", e);
                return body;
            }
        }
        
        // 包装其他类型的响应
        ApiResponse<Object> apiResponse = ApiResponse.success(body);
        apiResponse.setPath(path);
        return apiResponse;
    }
}

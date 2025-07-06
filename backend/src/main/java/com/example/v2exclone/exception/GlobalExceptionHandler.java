package com.example.v2exclone.exception;

import com.example.v2exclone.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(
            BusinessException e, HttpServletRequest request) {
        log.warn("业务异常: {} - {}", e.getCode(), e.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(e.getCode(), e.getMessage())
                .path(request.getRequestURI());
        
        return ResponseEntity.status(getHttpStatus(e.getCode())).body(response);
    }

    /**
     * 参数校验异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("参数校验失败: {}", message);
        
        ApiResponse<Object> response = ApiResponse.badRequest("参数校验失败: " + message)
                .path(request.getRequestURI());
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 绑定异常处理
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Object>> handleBindException(
            BindException e, HttpServletRequest request) {
        
        String message = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("参数绑定失败: {}", message);
        
        ApiResponse<Object> response = ApiResponse.badRequest("参数绑定失败: " + message)
                .path(request.getRequestURI());
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 缺少请求参数异常处理
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParameterException(
            MissingServletRequestParameterException e, HttpServletRequest request) {
        
        log.warn("缺少请求参数: {}", e.getParameterName());
        
        ApiResponse<Object> response = ApiResponse.badRequest("缺少必需参数: " + e.getParameterName())
                .path(request.getRequestURI());
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 参数类型不匹配异常处理
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        
        log.warn("参数类型不匹配: {} 应该是 {}", e.getName(), e.getRequiredType().getSimpleName());
        
        ApiResponse<Object> response = ApiResponse.badRequest(
                String.format("参数 %s 类型错误，应该是 %s", e.getName(), e.getRequiredType().getSimpleName()))
                .path(request.getRequestURI());
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 认证异常处理
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(
            AuthenticationException e, HttpServletRequest request) {
        
        log.warn("认证失败: {}", e.getMessage());
        
        ApiResponse<Object> response = ApiResponse.unauthorized("认证失败: " + e.getMessage())
                .path(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * 访问拒绝异常处理
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
            AccessDeniedException e, HttpServletRequest request) {
        
        log.warn("访问被拒绝: {}", e.getMessage());
        
        ApiResponse<Object> response = ApiResponse.forbidden("访问被拒绝: " + e.getMessage())
                .path(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * 404异常处理
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundException(
            NoHandlerFoundException e, HttpServletRequest request) {
        
        log.warn("请求路径不存在: {}", e.getRequestURL());
        
        ApiResponse<Object> response = ApiResponse.notFound("请求的资源不存在")
                .path(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * HTTP方法不支持异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        
        log.warn("HTTP方法不支持: {}", e.getMethod());
        
        ApiResponse<Object> response = ApiResponse.error(405, "HTTP方法不支持: " + e.getMethod())
                .path(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    /**
     * 通用异常处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(
            Exception e, HttpServletRequest request) {
        
        log.error("系统异常: ", e);
        
        ApiResponse<Object> response = ApiResponse.error("系统内部错误，请稍后重试")
                .path(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 根据错误码获取HTTP状态码
     */
    private HttpStatus getHttpStatus(Integer code) {
        if (code == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        
        return switch (code) {
            case 400 -> HttpStatus.BAD_REQUEST;
            case 401 -> HttpStatus.UNAUTHORIZED;
            case 403 -> HttpStatus.FORBIDDEN;
            case 404 -> HttpStatus.NOT_FOUND;
            case 405 -> HttpStatus.METHOD_NOT_ALLOWED;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}

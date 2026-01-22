package com.demo.exception;

/**
 * 资源未找到异常
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s未找到: %s = %s", resource, field, value));
    }
}

package com.marathon.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 业务异常
 */
@AllArgsConstructor
@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 异常码
     */
    private int code;

    /**
     * 具体异常信息
     */
    private String message;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
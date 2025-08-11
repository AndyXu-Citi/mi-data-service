package com.marathon.common.exception;

/**
 * 未授权异常
 *
 * @author marathon
 */
public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException() {
        super("未授权，请先登录");
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
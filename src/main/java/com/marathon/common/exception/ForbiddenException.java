package com.marathon.common.exception;

/**
 * 无权限异常
 *
 * @author marathon
 */
public class ForbiddenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException() {
        super("无权限访问");
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
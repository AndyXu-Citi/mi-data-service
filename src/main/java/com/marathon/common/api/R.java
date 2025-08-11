package com.marathon.common.api;

import java.io.Serializable;

/**
 * 通用响应对象
 *
 * @author marathon
 */
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS = 200;

    /**
     * 失败
     */
    public static final int FAIL = 500;

    /**
     * 未认证
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 无权限
     */
    public static final int FORBIDDEN = 403;

    /**
     * 状态码
     */
    private int code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T data, String message) {
        return restResult(data, SUCCESS, message);
    }

    public static <T> R<T> fail() {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String message) {
        return restResult(null, FAIL, message);
    }

    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> R<T> fail(T data, String message) {
        return restResult(data, FAIL, message);
    }

    public static <T> R<T> fail(int code, String message) {
        return restResult(null, code, message);
    }

    public static <T> R<T> unauthorized(String message) {
        return restResult(null, UNAUTHORIZED, message);
    }

    public static <T> R<T> forbidden(String message) {
        return restResult(null, FORBIDDEN, message);
    }

    private static <T> R<T> restResult(T data, int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMessage(message);
        return r;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
package com.marathon.common.enums;

public enum EventStatus {
    NOT_STARTED(1, "未开始"),
    REGISTRATION_OPEN(2, "报名中"),
    REGISTRATION_CLOSED(3, "报名结束"),
    IN_PROGRESS(4, "比赛中"),
    COMPLETED(5, "已结束");

    private final int code;
    private final String description;

    // 构造方法
    EventStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    // 获取编码
    public int getCode() {
        return code;
    }

    // 获取描述
    public String getDescription() {
        return description;
    }

    // 根据编码获取对应的枚举实例
    public static EventStatus getByCode(int code) {
        for (EventStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的赛事状态编码: " + code);
    }
}


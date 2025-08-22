package com.marathon.common.enums;

public enum EventLevel {
    PLATINUM(1, "白金"),
    GOLD(2, "金标"),
    ELITE(3, "精英标"),
    BRAND(4, "标牌"),
    CAA(5, "田协");

    private final int code;
    private final String description;

    // 构造方法
    EventLevel(int code, String description) {
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
    public static EventLevel getByCode(int code) {
        for (EventLevel level : values()) {
            if (level.code == code) {
                return level;
            }
        }
        throw new IllegalArgumentException("无效的赛事级别编码: " + code);
    }
}


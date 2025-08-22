package com.marathon.common.enums;

public enum EventType {
    MARATHON(1, "马拉松"),
    HALF_MARATHON(2, "半程马拉松"),
    HEALTH_RUN(3, "健康跑"),
    TRAIL_RUN(4, "越野跑"),
    OTHER(5, "其他");

    private final int code;
    private final String description;

    // 构造方法
    EventType(int code, String description) {
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
    public static EventType getByCode(int code) {
        for (EventType eventType : values()) {
            if (eventType.code == code) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("无效的赛事类型编码: " + code);

    }
}

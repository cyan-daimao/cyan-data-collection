package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 告警级别
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum AlertLevel {
    INFO("INFO", "信息"),
    WARN("WARN", "警告"),
    ERROR("ERROR", "错误");

    private final String code;
    private final String desc;

    public static AlertLevel of(String code) {
        for (AlertLevel value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

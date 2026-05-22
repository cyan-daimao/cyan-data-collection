package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件状态
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum EventStatus {
    DRAFT("DRAFT", "草稿"),
    REVIEWING("REVIEWING", "待评审"),
    PUBLISHED("PUBLISHED", "已发布"),
    DEPRECATED("DEPRECATED", "已废弃");

    private final String code;
    private final String desc;

    public static EventStatus of(String code) {
        for (EventStatus value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

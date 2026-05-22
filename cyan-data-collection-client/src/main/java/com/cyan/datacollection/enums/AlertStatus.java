package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 告警状态
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum AlertStatus {
    OPEN("OPEN", "未关闭"),
    CLOSED("CLOSED", "已关闭");

    private final String code;
    private final String desc;

    public static AlertStatus of(String code) {
        for (AlertStatus value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

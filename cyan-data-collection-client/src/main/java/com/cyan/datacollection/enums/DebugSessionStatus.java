package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Debug会话状态
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DebugSessionStatus {
    ACTIVE("ACTIVE", "活跃"),
    EXPIRED("EXPIRED", "已过期");

    private final String code;
    private final String desc;

    public static DebugSessionStatus of(String code) {
        for (DebugSessionStatus value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

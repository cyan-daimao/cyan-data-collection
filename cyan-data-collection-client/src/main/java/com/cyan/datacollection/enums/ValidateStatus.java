package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 校验状态
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ValidateStatus {
    PASS("PASS", "通过"),
    WARN("WARN", "警告"),
    FAIL("FAIL", "失败"),
    UNKNOWN("UNKNOWN", "未知");

    private final String code;
    private final String desc;

    public static ValidateStatus of(String code) {
        for (ValidateStatus value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

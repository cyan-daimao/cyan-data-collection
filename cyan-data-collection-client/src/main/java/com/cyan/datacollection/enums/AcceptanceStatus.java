package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验收任务状态
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum AcceptanceStatus {
    PENDING("PENDING", "待执行"),
    RUNNING("RUNNING", "执行中"),
    PASS("PASS", "通过"),
    FAIL("FAIL", "失败");

    private final String code;
    private final String desc;

    public static AcceptanceStatus of(String code) {
        for (AcceptanceStatus value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

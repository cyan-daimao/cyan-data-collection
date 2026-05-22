package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 告警类型
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum AlertType {
    NO_DATA("NO_DATA", "无数据"),
    FAIL_RATE_HIGH("FAIL_RATE_HIGH", "失败率过高"),
    PROPERTY_MISSING("PROPERTY_MISSING", "属性缺失");

    private final String code;
    private final String desc;

    public static AlertType of(String code) {
        for (AlertType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 属性状态
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum PropertyStatus {
    DRAFT("DRAFT", "草稿"),
    REVIEWING("REVIEWING", "待评审"),
    PUBLISHED("PUBLISHED", "已发布"),
    DEPRECATED("DEPRECATED", "已废弃");

    private final String code;
    private final String desc;

    public static PropertyStatus of(String code) {
        for (PropertyStatus value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

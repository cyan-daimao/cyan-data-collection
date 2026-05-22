package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 环境
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum Environment {
    DEV("DEV", "开发环境"),
    TEST("TEST", "测试环境"),
    PRE("PRE", "预发环境"),
    PROD("PROD", "生产环境");

    private final String code;
    private final String desc;

    public static Environment of(String code) {
        for (Environment value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据类型
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DataType {
    STRING("STRING", "字符串"),
    NUMBER("NUMBER", "数值"),
    BOOLEAN("BOOLEAN", "布尔"),
    DATE("DATE", "日期"),
    DATETIME("DATETIME", "日期时间"),
    ENUM("ENUM", "枚举"),
    ARRAY("ARRAY", "数组"),
    OBJECT("OBJECT", "对象");

    private final String code;
    private final String desc;

    public static DataType of(String code) {
        for (DataType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 属性类型
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum PropertyType {
    EVENT("EVENT", "事件属性"),
    USER("USER", "用户属性"),
    DEVICE("DEVICE", "设备属性"),
    COMMON("COMMON", "公共属性");

    private final String code;
    private final String desc;

    public static PropertyType of(String code) {
        for (PropertyType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

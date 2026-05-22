package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 终端类型
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum TerminalType {
    WEB("WEB", "Web端"),
    IOS("IOS", "iOS端"),
    ANDROID("ANDROID", "Android端"),
    MINI_PROGRAM("MINI_PROGRAM", "小程序"),
    SERVER("SERVER", "服务端");

    private final String code;
    private final String desc;

    public static TerminalType of(String code) {
        for (TerminalType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

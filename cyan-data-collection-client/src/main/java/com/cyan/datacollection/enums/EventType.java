package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件类型
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum EventType {
    PAGE_VIEW("PAGE_VIEW", "页面浏览"),
    CLICK("CLICK", "点击"),
    SUBMIT("SUBMIT", "提交"),
    SEARCH("SEARCH", "搜索"),
    TRANSACTION("TRANSACTION", "交易"),
    SYSTEM("SYSTEM", "系统"),
    CUSTOM("CUSTOM", "自定义");

    private final String code;
    private final String desc;

    public static EventType of(String code) {
        for (EventType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

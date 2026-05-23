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
    PAGE_ENTER("PAGE_ENTER", "页面进入"),
    PAGE_EXIT("PAGE_EXIT", "页面退出"),
    CLICK("CLICK", "点击"),
    EXPOSURE("EXPOSURE", "曝光"),
    SUBMIT("SUBMIT", "提交"),
    SEARCH("SEARCH", "搜索"),
    TRANSACTION("TRANSACTION", "交易"),
    SYSTEM("SYSTEM", "系统"),
    BUSINESS("BUSINESS", "后端业务"),
    OTHER("OTHER", "其他"),
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

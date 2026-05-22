package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 埋点需求状态
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DemandStatus {
    DRAFT("DRAFT", "草稿"),
    DESIGNING("DESIGNING", "设计中"),
    REVIEWING("REVIEWING", "待评审"),
    DEVELOPING("DEVELOPING", "开发中"),
    ACCEPTING("ACCEPTING", "待验收"),
    RELEASING("RELEASING", "待发布"),
    ONLINE("ONLINE", "已上线"),
    CLOSED("CLOSED", "已关闭");

    private final String code;
    private final String desc;

    public static DemandStatus of(String code) {
        for (DemandStatus value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

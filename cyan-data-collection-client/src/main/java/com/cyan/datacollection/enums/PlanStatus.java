package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 埋点方案状态
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum PlanStatus {
    DRAFT("DRAFT", "草稿"),
    REVIEWING("REVIEWING", "待评审"),
    DEVELOPING("DEVELOPING", "开发中"),
    ACCEPTING("ACCEPTING", "待验收"),
    RELEASING("RELEASING", "待发布"),
    PUBLISHED("PUBLISHED", "已发布"),
    CLOSED("CLOSED", "已关闭");

    private final String code;
    private final String desc;

    public static PlanStatus of(String code) {
        for (PlanStatus value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

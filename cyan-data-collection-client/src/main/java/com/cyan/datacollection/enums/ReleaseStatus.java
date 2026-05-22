package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 发布版本状态
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ReleaseStatus {
    DRAFT("DRAFT", "草稿"),
    SUBMITTED("SUBMITTED", "已提交"),
    PUBLISHED("PUBLISHED", "已发布"),
    ROLLED_BACK("ROLLED_BACK", "已回滚"),
    CANCELED("CANCELED", "已取消");

    private final String code;
    private final String desc;

    public static ReleaseStatus of(String code) {
        for (ReleaseStatus value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 同步状态
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum SyncStatus {

    /**
     * 待同步
     */
    PENDING("PENDING", "待同步"),

    /**
     * 同步成功
     */
    SUCCESS("SUCCESS", "同步成功"),

    /**
     * 同步失败
     */
    FAILED("FAILED", "同步失败");

    private final String code;
    private final String desc;

    /**
     * 根据编码解析
     *
     * @param code 编码
     * @return 同步状态
     */
    public static SyncStatus of(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        for (SyncStatus value : values()) {
            if (value.code.equalsIgnoreCase(code) || value.name().equalsIgnoreCase(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("未知同步状态: " + code);
    }
}

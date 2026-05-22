package com.cyan.datacollection.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 采集指标链路状态
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum MetricPipelineStatus {

    DRAFT("DRAFT", "草稿"),
    TABLE_CREATED("TABLE_CREATED", "表已创建"),
    JOB_CREATED("JOB_CREATED", "作业已创建"),
    RUNNING("RUNNING", "运行中"),
    FAILED("FAILED", "失败");

    private final String code;
    private final String desc;

    public static MetricPipelineStatus of(String code) {
        for (MetricPipelineStatus value : values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }
}

package com.cyan.datacollection.application.mapping.cmd;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 事件同步指标命令
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class EventMetricSyncCmd {

    /**
     * 指标编码
     */
    private String metricCode;

    /**
     * 指标名称
     */
    private String metricName;

    /**
     * 主题域编码
     */
    private String subjectCode;

    /**
     * 统计函数
     */
    private String statFunc;

    /**
     * 负责人
     */
    private String owner;

    /**
     * 数据密级
     */
    private String securityLevel;

    /**
     * 操作人
     */
    private String operator;
}

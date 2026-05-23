package com.cyan.datacollection.application.mapping.bo;

import com.cyan.datacollection.enums.SyncStatus;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 事件指标映射业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class EventMetricMappingBO {

    /**
     * 主键
     */
    private String id;

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 指标ID
     */
    private String metricId;

    /**
     * 指标编码
     */
    private String metricCode;

    /**
     * 同步状态
     */
    private SyncStatus syncStatus;

    /**
     * 错误信息
     */
    private String errorMessage;
}

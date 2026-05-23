package com.cyan.datacollection.application.mapping;

import com.cyan.datacollection.application.mapping.bo.EventMetricMappingBO;
import com.cyan.datacollection.application.mapping.bo.PropertyDimensionMappingBO;
import com.cyan.datacollection.application.mapping.cmd.EventMetricSyncCmd;
import com.cyan.datacollection.application.mapping.cmd.PropertyDimensionSyncCmd;

/**
 * 采集指标平台映射服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingMetricMappingService {

    /**
     * 属性同步为维度
     */
    PropertyDimensionMappingBO syncPropertyDimension(String propertyId, PropertyDimensionSyncCmd cmd);

    /**
     * 查询属性维度映射
     */
    PropertyDimensionMappingBO getPropertyDimensionMapping(String propertyId);

    /**
     * 事件同步为指标
     */
    EventMetricMappingBO syncEventMetric(String eventId, EventMetricSyncCmd cmd);

    /**
     * 查询事件指标映射
     */
    EventMetricMappingBO getEventMetricMapping(String eventId);
}

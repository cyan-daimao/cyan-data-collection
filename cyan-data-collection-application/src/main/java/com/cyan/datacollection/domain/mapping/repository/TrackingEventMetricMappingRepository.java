package com.cyan.datacollection.domain.mapping.repository;

import com.cyan.datacollection.domain.mapping.TrackingEventMetricMapping;

/**
 * 采集事件指标映射仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingEventMetricMappingRepository {

    /**
     * 根据事件ID查询
     */
    TrackingEventMetricMapping findByEventId(String eventId);

    /**
     * 根据指标编码查询
     */
    TrackingEventMetricMapping findByMetricCode(String metricCode);

    /**
     * 保存
     */
    TrackingEventMetricMapping save(TrackingEventMetricMapping mapping);

    /**
     * 更新
     */
    TrackingEventMetricMapping update(TrackingEventMetricMapping mapping);
}

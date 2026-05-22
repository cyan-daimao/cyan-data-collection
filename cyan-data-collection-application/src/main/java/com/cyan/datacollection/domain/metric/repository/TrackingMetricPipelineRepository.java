package com.cyan.datacollection.domain.metric.repository;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.domain.metric.TrackingMetricPipeline;
import com.cyan.datacollection.domain.metric.query.TrackingMetricPipelinePageQuery;

/**
 * 采集指标链路仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingMetricPipelineRepository {

    TrackingMetricPipeline findById(String id);

    TrackingMetricPipeline findByMetricCode(String metricCode);

    Page<TrackingMetricPipeline> page(TrackingMetricPipelinePageQuery query);

    TrackingMetricPipeline save(TrackingMetricPipeline pipeline);

    TrackingMetricPipeline update(TrackingMetricPipeline pipeline);

    void deleteById(String id);
}

package com.cyan.datacollection.domain.quality.repository;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.domain.quality.TrackingQualityMetric;
import com.cyan.datacollection.domain.quality.query.TrackingQualityMetricPageQuery;

/**
 * 质量指标仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingQualityMetricRepository {

    /**
     * 根据ID查询
     */
    TrackingQualityMetric findById(String id);

    /**
     * 分页查询
     */
    Page<TrackingQualityMetric> page(TrackingQualityMetricPageQuery query);

    /**
     * 保存
     */
    TrackingQualityMetric save(TrackingQualityMetric metric);

    /**
     * 更新
     */
    TrackingQualityMetric update(TrackingQualityMetric metric);

    /**
     * 根据ID删除
     */
    void deleteById(String id);
}

package com.cyan.datacollection.domain.collect.repository;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.domain.collect.TrackingEventSample;
import com.cyan.datacollection.domain.collect.query.TrackingEventSamplePageQuery;

import java.util.List;

/**
 * 事件样本仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingEventSampleRepository {

    TrackingEventSample findById(String id);

    Page<TrackingEventSample> page(TrackingEventSamplePageQuery query);

    TrackingEventSample save(TrackingEventSample sample);

    List<TrackingEventSample> saveBatch(List<TrackingEventSample> samples);

    /**
     * 根据 debugToken 查询样本列表
     */
    List<TrackingEventSample> findByDebugToken(String debugToken);
}

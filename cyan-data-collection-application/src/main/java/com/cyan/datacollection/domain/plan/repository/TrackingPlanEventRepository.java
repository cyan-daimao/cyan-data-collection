package com.cyan.datacollection.domain.plan.repository;

import com.cyan.datacollection.domain.plan.TrackingPlanEventRelation;

import java.util.List;

/**
 * 方案事件关系仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingPlanEventRepository {

    /**
     * 根据方案ID查询所有事件关系
     */
    List<TrackingPlanEventRelation> findByPlanId(String planId);

    /**
     * 保存方案事件关系
     */
    TrackingPlanEventRelation save(TrackingPlanEventRelation relation);

    /**
     * 根据方案ID和事件ID逻辑删除关系
     */
    void deleteByPlanIdAndEventId(String planId, String eventId);

    /**
     * 判断方案事件关系是否已存在
     */
    boolean existsByPlanIdAndEventId(String planId, String eventId);
}

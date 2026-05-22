package com.cyan.datacollection.domain.plan.repository;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.domain.plan.TrackingPlan;
import com.cyan.datacollection.domain.plan.query.TrackingPlanPageQuery;

/**
 * 埋点方案仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingPlanRepository {

    /**
     * 根据ID查询
     */
    TrackingPlan findById(String id);

    /**
     * 分页查询
     */
    Page<TrackingPlan> page(TrackingPlanPageQuery query);

    /**
     * 根据编码查询
     */
    TrackingPlan findByCode(String planCode);

    /**
     * 保存
     */
    TrackingPlan save(TrackingPlan plan);

    /**
     * 更新
     */
    TrackingPlan update(TrackingPlan plan);

    /**
     * 根据ID删除
     */
    void deleteById(String id);

    /**
     * 获取当天最大序号
     */
    int findMaxSeqToday();
}

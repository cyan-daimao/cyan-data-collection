package com.cyan.datacollection.domain.acceptance.repository;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.domain.acceptance.TrackingAcceptanceTask;
import com.cyan.datacollection.domain.acceptance.query.TrackingAcceptanceTaskPageQuery;

/**
 * 验收任务仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingAcceptanceTaskRepository {

    /**
     * 根据ID查询
     */
    TrackingAcceptanceTask findById(String id);

    /**
     * 分页查询
     */
    Page<TrackingAcceptanceTask> page(TrackingAcceptanceTaskPageQuery query);

    /**
     * 保存
     */
    TrackingAcceptanceTask save(TrackingAcceptanceTask task);

    /**
     * 更新
     */
    TrackingAcceptanceTask update(TrackingAcceptanceTask task);

    /**
     * 根据ID删除
     */
    void deleteById(String id);

    /**
     * 获取当天最大序号
     */
    int findMaxSeqToday();
}

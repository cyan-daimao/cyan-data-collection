package com.cyan.datacollection.domain.acceptance.repository;

import com.cyan.datacollection.domain.acceptance.TrackingAcceptanceResult;

import java.util.List;

/**
 * 验收结果仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingAcceptanceResultRepository {

    /**
     * 根据ID查询
     */
    TrackingAcceptanceResult findById(String id);

    /**
     * 根据任务ID查询结果列表
     */
    List<TrackingAcceptanceResult> findByTaskId(String taskId);

    /**
     * 保存
     */
    TrackingAcceptanceResult save(TrackingAcceptanceResult result);

    /**
     * 批量保存
     */
    List<TrackingAcceptanceResult> saveBatch(List<TrackingAcceptanceResult> results);

    /**
     * 根据任务ID删除
     */
    void deleteByTaskId(String taskId);
}

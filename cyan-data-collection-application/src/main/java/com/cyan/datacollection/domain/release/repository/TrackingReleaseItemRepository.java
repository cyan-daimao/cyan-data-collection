package com.cyan.datacollection.domain.release.repository;

import com.cyan.datacollection.domain.release.TrackingReleaseItem;

import java.util.List;

/**
 * 埋点发布版本明细仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingReleaseItemRepository {

    /**
     * 根据发布ID查询明细列表
     */
    List<TrackingReleaseItem> findByReleaseId(String releaseId);

    /**
     * 保存
     */
    TrackingReleaseItem save(TrackingReleaseItem item);

    /**
     * 批量保存
     */
    void saveBatch(List<TrackingReleaseItem> items);

    /**
     * 根据ID删除
     */
    void deleteById(String id);

    /**
     * 根据发布ID删除
     */
    void deleteByReleaseId(String releaseId);
}

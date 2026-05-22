package com.cyan.datacollection.domain.release.repository;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.domain.release.TrackingRelease;
import com.cyan.datacollection.domain.release.query.TrackingReleasePageQuery;

/**
 * 埋点发布版本仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingReleaseRepository {

    /**
     * 根据ID查询
     */
    TrackingRelease findById(String id);

    /**
     * 分页查询
     */
    Page<TrackingRelease> page(TrackingReleasePageQuery query);

    /**
     * 保存
     */
    TrackingRelease save(TrackingRelease release);

    /**
     * 更新
     */
    TrackingRelease update(TrackingRelease release);

    /**
     * 根据ID删除
     */
    void deleteById(String id);

    /**
     * 获取当天最大序号
     */
    int findMaxSeqToday();
}

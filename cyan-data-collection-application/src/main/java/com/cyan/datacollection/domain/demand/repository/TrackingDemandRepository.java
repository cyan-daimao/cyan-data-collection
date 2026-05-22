package com.cyan.datacollection.domain.demand.repository;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.domain.demand.TrackingDemand;
import com.cyan.datacollection.domain.demand.query.TrackingDemandPageQuery;

/**
 * 埋点需求仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingDemandRepository {

    /**
     * 根据ID查询
     */
    TrackingDemand findById(String id);

    /**
     * 分页查询
     */
    Page<TrackingDemand> page(TrackingDemandPageQuery query);

    /**
     * 根据编码查询
     */
    TrackingDemand findByCode(String demandCode);

    /**
     * 保存
     */
    TrackingDemand save(TrackingDemand demand);

    /**
     * 更新
     */
    TrackingDemand update(TrackingDemand demand);

    /**
     * 根据ID删除
     */
    void deleteById(String id);

    /**
     * 获取当天最大序号
     */
    int findMaxSeqToday();
}

package com.cyan.datacollection.application.acceptance;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.application.acceptance.bo.TrackingAcceptanceTaskBO;
import com.cyan.datacollection.application.acceptance.cmd.TrackingAcceptanceTaskCmd;
import com.cyan.datacollection.domain.acceptance.query.TrackingAcceptanceTaskPageQuery;

/**
 * 验收任务服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingAcceptanceService {

    /**
     * 分页查询
     */
    Page<TrackingAcceptanceTaskBO> page(TrackingAcceptanceTaskPageQuery query);

    /**
     * 创建验收任务
     */
    TrackingAcceptanceTaskBO create(TrackingAcceptanceTaskCmd cmd);

    /**
     * 详情（包含结果列表）
     */
    TrackingAcceptanceTaskBO detail(String id);

    /**
     * 执行验收
     */
    TrackingAcceptanceTaskBO run(String id);

    /**
     * 验收通过
     */
    TrackingAcceptanceTaskBO approve(String id);

    /**
     * 验收驳回
     */
    TrackingAcceptanceTaskBO reject(String id);
}

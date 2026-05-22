package com.cyan.datacollection.application.plan;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.application.plan.bo.TrackingPlanBO;
import com.cyan.datacollection.application.plan.cmd.TrackingPlanCmd;
import com.cyan.datacollection.application.plan.cmd.TrackingPlanEventConfigCmd;
import com.cyan.datacollection.domain.plan.query.TrackingPlanPageQuery;

import java.util.List;

/**
 * 埋点方案服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingPlanService {

    /**
     * 分页查询
     */
    Page<TrackingPlanBO> page(TrackingPlanPageQuery query);

    /**
     * 创建
     */
    TrackingPlanBO create(TrackingPlanCmd cmd);

    /**
     * 更新
     */
    TrackingPlanBO update(String id, TrackingPlanCmd cmd);

    /**
     * 详情
     */
    TrackingPlanBO detail(String id);

    /**
     * 添加事件到方案
     */
    void addEvent(String id, List<String> eventIds);

    /**
     * 从方案移除事件
     */
    void removeEvent(String id, String eventId);

    /**
     * 配置事件属性
     */
    void configEventProperties(String id, String eventId, List<TrackingPlanEventConfigCmd> configs);

    /**
     * 提交评审
     */
    TrackingPlanBO submitReview(String id);

    /**
     * 评审通过
     */
    TrackingPlanBO approve(String id);

    /**
     * 评审驳回
     */
    TrackingPlanBO reject(String id);
}

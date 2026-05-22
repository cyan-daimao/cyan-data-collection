package com.cyan.datacollection.application.event;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.application.event.bo.TrackingEventBO;
import com.cyan.datacollection.application.event.cmd.TrackingEventCmd;
import com.cyan.datacollection.domain.event.query.TrackingEventPageQuery;

import java.util.List;

/**
 * 事件定义服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingEventService {

    /**
     * 分页查询
     */
    Page<TrackingEventBO> page(TrackingEventPageQuery query);

    /**
     * 创建
     */
    TrackingEventBO create(TrackingEventCmd cmd);

    /**
     * 更新
     */
    TrackingEventBO update(String id, TrackingEventCmd cmd);

    /**
     * 详情
     */
    TrackingEventBO detail(String id);

    /**
     * 发布
     */
    TrackingEventBO publish(String id);

    /**
     * 废弃
     */
    TrackingEventBO deprecate(String id);

    /**
     * 使用情况
     */
    TrackingEventBO.UsageBO usage(String id);

    /**
     * 根据ID列表查询
     */
    List<TrackingEventBO> findByIds(List<String> ids);
}

package com.cyan.datacollection.domain.event.repository;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.domain.event.TrackingEvent;
import com.cyan.datacollection.domain.event.query.TrackingEventPageQuery;

import java.util.List;

/**
 * 事件定义仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingEventRepository {

    /**
     * 根据ID查询
     */
    TrackingEvent findById(String id);

    /**
     * 分页查询
     */
    Page<TrackingEvent> page(TrackingEventPageQuery query);

    /**
     * 根据编码查询
     */
    TrackingEvent findByCode(String eventCode);

    /**
     * 保存
     */
    TrackingEvent save(TrackingEvent event);

    /**
     * 更新
     */
    TrackingEvent update(TrackingEvent event);

    /**
     * 根据ID删除
     */
    void deleteById(String id);

    /**
     * 根据ID列表查询
     */
    List<TrackingEvent> findByIds(List<String> ids);
}

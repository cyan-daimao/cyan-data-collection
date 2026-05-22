package com.cyan.datacollection.domain.eventproperty.repository;

import com.cyan.datacollection.domain.eventproperty.EventPropertyRule;
import com.cyan.datacollection.domain.eventproperty.TrackingEventProperty;

import java.util.List;

/**
 * 事件属性关系仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingEventPropertyRepository {

    /**
     * 根据ID查询
     */
    TrackingEventProperty findById(String id);

    /**
     * 根据事件ID查询
     */
    List<TrackingEventProperty> findByEventId(String eventId);

    /**
     * 根据属性ID查询
     */
    List<TrackingEventProperty> findByPropertyId(String propertyId);

    /**
     * 保存
     */
    TrackingEventProperty save(TrackingEventProperty eventProperty);

    /**
     * 更新
     */
    TrackingEventProperty update(TrackingEventProperty eventProperty);

    /**
     * 根据ID删除
     */
    void deleteById(String id);

    /**
     * 根据事件ID删除
     */
    void deleteByEventId(String eventId);

    /**
     * 根据属性ID删除
     */
    void deleteByPropertyId(String propertyId);

    /**
     * 查询事件属性规则（关联属性定义）
     */
    List<EventPropertyRule> findPropertyRulesByEventId(String eventId);
}

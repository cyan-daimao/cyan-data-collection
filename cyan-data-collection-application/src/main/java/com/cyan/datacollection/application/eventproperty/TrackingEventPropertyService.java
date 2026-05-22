package com.cyan.datacollection.application.eventproperty;

import com.cyan.datacollection.application.eventproperty.bo.EventPropertyBO;
import com.cyan.datacollection.application.eventproperty.cmd.EventPropertyConfigCmd;

import java.util.List;

/**
 * 事件属性关系服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingEventPropertyService {

    /**
     * 配置事件属性（全量替换）
     *
     * @param eventId 事件ID
     * @param cmds    属性配置列表
     */
    void configProperties(String eventId, List<EventPropertyConfigCmd> cmds);

    /**
     * 查询事件属性列表
     *
     * @param eventId 事件ID
     * @return 属性BO列表（聚合属性定义 + 关系信息）
     */
    List<EventPropertyBO> listProperties(String eventId);
}

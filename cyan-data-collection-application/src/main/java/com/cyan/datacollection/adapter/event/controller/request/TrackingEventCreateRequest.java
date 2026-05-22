package com.cyan.datacollection.adapter.event.controller.request;

import com.cyan.datacollection.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 创建事件请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingEventCreateRequest {

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件类型
     */
    private EventType eventType;

    /**
     * 业务域
     */
    private String businessDomain;

    /**
     * 业务描述
     */
    private String description;

    /**
     * 触发时机
     */
    private String triggerTiming;

    /**
     * 支持端类型
     */
    private List<String> terminalTypes;

    /**
     * 负责人
     */
    private String owner;

    /**
     * 是否核心事件
     */
    private Boolean isCore;
}

package com.cyan.datacollection.adapter.event.controller.dto;

import com.cyan.datacollection.enums.EventStatus;
import com.cyan.datacollection.enums.EventType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 事件定义DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingEventDTO {

    /**
     * 主键
     */
    private String id;

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

    /**
     * 状态
     */
    private EventStatus status;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;

    /**
     * 事件属性列表
     */
    private List<EventPropertyDTO> properties;
}

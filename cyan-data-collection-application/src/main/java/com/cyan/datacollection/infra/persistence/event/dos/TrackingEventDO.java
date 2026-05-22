package com.cyan.datacollection.infra.persistence.event.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyan.datacollection.enums.EventStatus;
import com.cyan.datacollection.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 事件定义表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("tracking_event")
public class TrackingEventDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 事件编码
     */
    @TableField("event_code")
    private String eventCode;

    /**
     * 事件名称
     */
    @TableField("event_name")
    private String eventName;

    /**
     * 事件类型
     */
    @TableField("event_type")
    private EventType eventType;

    /**
     * 业务域
     */
    @TableField("business_domain")
    private String businessDomain;

    /**
     * 业务描述
     */
    @TableField("description")
    private String description;

    /**
     * 触发时机
     */
    @TableField("trigger_timing")
    private String triggerTiming;

    /**
     * 支持端类型，逗号分隔
     */
    @TableField("terminal_types")
    private String terminalTypes;

    /**
     * 负责人
     */
    @TableField("owner")
    private String owner;

    /**
     * 是否核心事件
     */
    @TableField("is_core")
    private Boolean isCore;

    /**
     * 状态
     */
    @TableField("status")
    private EventStatus status;

    /**
     * 版本号
     */
    @TableField("version")
    private Integer version;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private String updateBy;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除
     */
    @TableField("deleted_at")
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;
}

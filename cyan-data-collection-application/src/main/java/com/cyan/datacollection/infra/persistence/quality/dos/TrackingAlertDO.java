package com.cyan.datacollection.infra.persistence.quality.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyan.datacollection.enums.AlertLevel;
import com.cyan.datacollection.enums.AlertStatus;
import com.cyan.datacollection.enums.AlertType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 埋点质量告警表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("tracking_alert")
public class TrackingAlertDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 告警类型
     */
    @TableField("alert_type")
    private AlertType alertType;

    /**
     * 应用编码
     */
    @TableField("app_code")
    private String appCode;

    /**
     * 事件编码
     */
    @TableField("event_code")
    private String eventCode;

    /**
     * 告警级别
     */
    @TableField("alert_level")
    private AlertLevel alertLevel;

    /**
     * 告警信息
     */
    @TableField("alert_message")
    private String alertMessage;

    /**
     * 状态
     */
    @TableField("status")
    private AlertStatus status;

    /**
     * 触发时间
     */
    @TableField("triggered_at")
    private LocalDateTime triggeredAt;

    /**
     * 关闭时间
     */
    @TableField("closed_at")
    private LocalDateTime closedAt;

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
     * 逻辑删除时间
     */
    @TableField("deleted_at")
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;
}

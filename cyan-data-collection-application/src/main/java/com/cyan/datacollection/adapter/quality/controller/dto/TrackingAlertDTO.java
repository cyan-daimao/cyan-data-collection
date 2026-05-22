package com.cyan.datacollection.adapter.quality.controller.dto;

import com.cyan.datacollection.enums.AlertLevel;
import com.cyan.datacollection.enums.AlertStatus;
import com.cyan.datacollection.enums.AlertType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 质量告警DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingAlertDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 告警类型
     */
    private AlertType alertType;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 告警级别
     */
    private AlertLevel alertLevel;

    /**
     * 告警信息
     */
    private String alertMessage;

    /**
     * 状态
     */
    private AlertStatus status;

    /**
     * 触发时间
     */
    private LocalDateTime triggeredAt;

    /**
     * 关闭时间
     */
    private LocalDateTime closedAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}

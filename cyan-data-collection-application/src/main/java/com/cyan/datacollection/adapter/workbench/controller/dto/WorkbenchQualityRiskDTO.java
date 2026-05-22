package com.cyan.datacollection.adapter.workbench.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 工作台质量风险DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WorkbenchQualityRiskDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 告警类型
     */
    private String alertType;

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
    private String alertLevel;

    /**
     * 告警信息
     */
    private String alertMessage;

    /**
     * 触发时间
     */
    private LocalDateTime triggeredAt;
}

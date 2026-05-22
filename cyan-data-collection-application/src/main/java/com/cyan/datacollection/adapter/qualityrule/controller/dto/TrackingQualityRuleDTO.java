package com.cyan.datacollection.adapter.qualityrule.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 质量规则配置DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingQualityRuleDTO {

    private String id;
    private String ruleCode;
    private String ruleName;
    private String eventCode;
    private String appCode;
    private String alertType;
    private BigDecimal thresholdValue;
    private Integer timeWindowMinutes;
    private String alertLevel;
    private String notifyTargets;
    private Boolean isEnabled;
    private Boolean isCoreEventOnly;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

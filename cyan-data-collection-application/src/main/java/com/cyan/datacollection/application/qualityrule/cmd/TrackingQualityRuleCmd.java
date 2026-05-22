package com.cyan.datacollection.application.qualityrule.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 质量规则配置命令
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingQualityRuleCmd {

    private String ruleCode;
    private String ruleName;
    private String eventCode;
    private String appCode;
    private String alertType;
    private BigDecimal thresholdValue;
    private Integer timeWindowMinutes;
    private String alertLevel;
    private String notifyTargets;
    private Boolean isCoreEventOnly;
}

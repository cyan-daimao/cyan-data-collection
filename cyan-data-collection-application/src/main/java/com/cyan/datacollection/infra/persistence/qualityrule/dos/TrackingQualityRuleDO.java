package com.cyan.datacollection.infra.persistence.qualityrule.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 质量规则配置数据对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@TableName("tracking_quality_rule")
public class TrackingQualityRuleDO {

    private Long id;
    private String ruleCode;
    private String ruleName;
    private String eventCode;
    private String appCode;
    private String alertType;
    private BigDecimal thresholdValue;
    private Integer timeWindowMinutes;
    private String alertLevel;
    private String notifyTargets;
    private Integer isEnabled;
    private Integer isCoreEventOnly;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String createdBy;
    private String updatedBy;
}

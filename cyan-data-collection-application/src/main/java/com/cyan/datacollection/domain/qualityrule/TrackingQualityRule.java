package com.cyan.datacollection.domain.qualityrule;

import com.cyan.datacollection.domain.qualityrule.repository.TrackingQualityRuleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 质量规则配置领域对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingQualityRule {

    private String id;
    private String ruleCode;
    private String ruleName;
    private String eventCode;
    private String appCode;
    private String alertType;
    private java.math.BigDecimal thresholdValue;
    private Integer timeWindowMinutes;
    private String alertLevel;
    private String notifyTargets;
    private Boolean isEnabled;
    private Boolean isCoreEventOnly;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String createdBy;
    private String updatedBy;

    /**
     * 保存规则
     */
    public TrackingQualityRule save(TrackingQualityRuleRepository repository) {
        return repository.save(this);
    }

    /**
     * 更新规则
     */
    public TrackingQualityRule update(TrackingQualityRuleRepository repository) {
        return repository.update(this);
    }

    /**
     * 启用规则
     */
    public TrackingQualityRule enable(TrackingQualityRuleRepository repository) {
        this.isEnabled = true;
        return repository.update(this);
    }

    /**
     * 禁用规则
     */
    public TrackingQualityRule disable(TrackingQualityRuleRepository repository) {
        this.isEnabled = false;
        return repository.update(this);
    }
}

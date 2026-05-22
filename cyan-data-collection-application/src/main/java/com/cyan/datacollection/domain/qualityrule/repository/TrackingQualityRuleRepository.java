package com.cyan.datacollection.domain.qualityrule.repository;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.domain.qualityrule.TrackingQualityRule;
import com.cyan.datacollection.domain.qualityrule.query.TrackingQualityRulePageQuery;

import java.util.List;

/**
 * 质量规则配置仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingQualityRuleRepository {

    TrackingQualityRule findById(String id);

    Page<TrackingQualityRule> page(TrackingQualityRulePageQuery query);

    List<TrackingQualityRule> findEnabledRules();

    TrackingQualityRule save(TrackingQualityRule rule);

    TrackingQualityRule update(TrackingQualityRule rule);

    void deleteById(String id);
}

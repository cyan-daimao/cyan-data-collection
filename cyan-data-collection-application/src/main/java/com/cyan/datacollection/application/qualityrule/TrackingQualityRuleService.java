package com.cyan.datacollection.application.qualityrule;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.application.qualityrule.bo.TrackingQualityRuleBO;
import com.cyan.datacollection.application.qualityrule.cmd.TrackingQualityRuleCmd;
import com.cyan.datacollection.domain.qualityrule.query.TrackingQualityRulePageQuery;

import java.util.List;

/**
 * 质量规则配置服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingQualityRuleService {

    TrackingQualityRuleBO create(TrackingQualityRuleCmd cmd);

    TrackingQualityRuleBO update(String id, TrackingQualityRuleCmd cmd);

    TrackingQualityRuleBO getById(String id);

    Page<TrackingQualityRuleBO> page(TrackingQualityRulePageQuery query);

    TrackingQualityRuleBO enable(String id);

    TrackingQualityRuleBO disable(String id);

    List<TrackingQualityRuleBO> listEnabledRules();
}

package com.cyan.datacollection.infra.persistence.qualityrule.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.qualityrule.TrackingQualityRule;
import com.cyan.datacollection.infra.persistence.qualityrule.dos.TrackingQualityRuleDO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 质量规则配置基础设施层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingQualityRuleInfraConvert {

    TrackingQualityRule toDomain(TrackingQualityRuleDO dos);

    List<TrackingQualityRule> toDomainList(List<TrackingQualityRuleDO> dos);

    TrackingQualityRuleDO toDO(TrackingQualityRule domain);
}

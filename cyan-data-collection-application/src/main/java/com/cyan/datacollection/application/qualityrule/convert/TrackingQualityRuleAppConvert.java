package com.cyan.datacollection.application.qualityrule.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.qualityrule.bo.TrackingQualityRuleBO;
import com.cyan.datacollection.application.qualityrule.cmd.TrackingQualityRuleCmd;
import com.cyan.datacollection.domain.qualityrule.TrackingQualityRule;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 质量规则配置应用层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingQualityRuleAppConvert {

    TrackingQualityRuleAppConvert INSTANCE = Mappers.getMapper(TrackingQualityRuleAppConvert.class);

    TrackingQualityRuleBO toBO(TrackingQualityRule domain);

    TrackingQualityRule toDomain(TrackingQualityRuleCmd cmd);
}

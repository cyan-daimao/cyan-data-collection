package com.cyan.datacollection.adapter.qualityrule.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.adapter.qualityrule.controller.dto.TrackingQualityRuleDTO;
import com.cyan.datacollection.adapter.qualityrule.controller.request.TrackingQualityRulePageRequest;
import com.cyan.datacollection.adapter.qualityrule.controller.request.TrackingQualityRuleRequest;
import com.cyan.datacollection.application.qualityrule.bo.TrackingQualityRuleBO;
import com.cyan.datacollection.application.qualityrule.cmd.TrackingQualityRuleCmd;
import com.cyan.datacollection.domain.qualityrule.query.TrackingQualityRulePageQuery;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 质量规则配置适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingQualityRuleAdapterConvert {

    TrackingQualityRuleAdapterConvert INSTANCE = Mappers.getMapper(TrackingQualityRuleAdapterConvert.class);

    TrackingQualityRuleDTO toDTO(TrackingQualityRuleBO bo);

    TrackingQualityRuleCmd toCmd(TrackingQualityRuleRequest request);

    TrackingQualityRulePageQuery toPageQuery(TrackingQualityRulePageRequest request);
}

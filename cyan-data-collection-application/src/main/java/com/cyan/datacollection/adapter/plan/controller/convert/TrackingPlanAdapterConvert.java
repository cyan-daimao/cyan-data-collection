package com.cyan.datacollection.adapter.plan.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.plan.bo.TrackingPlanBO;
import com.cyan.datacollection.application.plan.cmd.TrackingPlanCmd;
import com.cyan.datacollection.application.plan.cmd.TrackingPlanEventConfigCmd;
import com.cyan.datacollection.adapter.plan.controller.dto.TrackingPlanDTO;
import com.cyan.datacollection.adapter.plan.controller.request.TrackingPlanCreateRequest;
import com.cyan.datacollection.adapter.plan.controller.request.TrackingPlanEventConfigRequest;
import com.cyan.datacollection.adapter.plan.controller.request.TrackingPlanUpdateRequest;
import com.cyan.datacollection.domain.plan.query.TrackingPlanPageQuery;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 埋点方案适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingPlanAdapterConvert {

    TrackingPlanAdapterConvert INSTANCE = Mappers.getMapper(TrackingPlanAdapterConvert.class);

    TrackingPlanDTO toClientDTO(TrackingPlanBO bo);

    List<TrackingPlanDTO> toClientDTOList(List<TrackingPlanBO> bos);

    TrackingPlanCmd toCmd(TrackingPlanCreateRequest request);

    TrackingPlanCmd toCmd(TrackingPlanUpdateRequest request);

    TrackingPlanPageQuery toPageQuery(com.cyan.datacollection.adapter.plan.controller.request.TrackingPlanPageQuery query);

    TrackingPlanEventConfigCmd toCmd(TrackingPlanEventConfigRequest request);
}

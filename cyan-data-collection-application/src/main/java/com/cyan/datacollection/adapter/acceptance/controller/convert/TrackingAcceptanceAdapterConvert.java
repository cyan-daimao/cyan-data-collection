package com.cyan.datacollection.adapter.acceptance.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.adapter.acceptance.controller.dto.TrackingAcceptanceResultDTO;
import com.cyan.datacollection.adapter.acceptance.controller.dto.TrackingAcceptanceTaskDTO;
import com.cyan.datacollection.adapter.acceptance.controller.request.TrackingAcceptanceTaskCreateRequest;
import com.cyan.datacollection.application.acceptance.bo.TrackingAcceptanceResultBO;
import com.cyan.datacollection.application.acceptance.bo.TrackingAcceptanceTaskBO;
import com.cyan.datacollection.application.acceptance.cmd.TrackingAcceptanceTaskCmd;
import com.cyan.datacollection.domain.acceptance.query.TrackingAcceptanceTaskPageQuery;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 验收任务适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingAcceptanceAdapterConvert {

    TrackingAcceptanceAdapterConvert INSTANCE = Mappers.getMapper(TrackingAcceptanceAdapterConvert.class);

    TrackingAcceptanceTaskDTO toClientDTO(TrackingAcceptanceTaskBO bo);

    List<TrackingAcceptanceTaskDTO> toTaskClientDTOList(List<TrackingAcceptanceTaskBO> bos);

    TrackingAcceptanceResultDTO toResultClientDTO(TrackingAcceptanceResultBO bo);

    List<TrackingAcceptanceResultDTO> toResultClientDTOList(List<TrackingAcceptanceResultBO> bos);

    TrackingAcceptanceTaskCmd toCmd(TrackingAcceptanceTaskCreateRequest request);

    TrackingAcceptanceTaskPageQuery toPageQuery(com.cyan.datacollection.adapter.acceptance.controller.request.TrackingAcceptanceTaskPageQuery query);
}

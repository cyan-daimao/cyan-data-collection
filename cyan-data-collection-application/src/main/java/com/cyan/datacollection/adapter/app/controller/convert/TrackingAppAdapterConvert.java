package com.cyan.datacollection.adapter.app.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.app.bo.TrackingAppBO;
import com.cyan.datacollection.application.app.cmd.TrackingAppCmd;
import com.cyan.datacollection.adapter.app.controller.dto.TrackingAppDTO;
import com.cyan.datacollection.adapter.app.controller.dto.TrackingAppIntegrationDTO;
import com.cyan.datacollection.adapter.app.controller.request.TrackingAppCreateRequest;
import com.cyan.datacollection.adapter.app.controller.request.TrackingAppUpdateRequest;
import com.cyan.datacollection.domain.app.query.TrackingAppPageQuery;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 接入应用适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingAppAdapterConvert {

    TrackingAppAdapterConvert INSTANCE = Mappers.getMapper(TrackingAppAdapterConvert.class);

    TrackingAppDTO toClientDTO(TrackingAppBO bo);

    List<TrackingAppDTO> toClientDTOList(List<TrackingAppBO> bos);

    TrackingAppCmd toCmd(TrackingAppCreateRequest request);

    TrackingAppCmd toCmd(TrackingAppUpdateRequest request);

    TrackingAppPageQuery toPageQuery(com.cyan.datacollection.adapter.app.controller.request.TrackingAppPageQuery query);

    TrackingAppIntegrationDTO toClientIntegrationDTO(TrackingAppBO.IntegrationBO bo);
}

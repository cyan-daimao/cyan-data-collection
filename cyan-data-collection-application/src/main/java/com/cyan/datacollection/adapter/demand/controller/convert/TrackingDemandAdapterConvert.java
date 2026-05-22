package com.cyan.datacollection.adapter.demand.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.demand.bo.TrackingDemandBO;
import com.cyan.datacollection.application.demand.cmd.TrackingDemandCmd;
import com.cyan.datacollection.adapter.demand.controller.dto.TrackingDemandDTO;
import com.cyan.datacollection.adapter.demand.controller.request.TrackingDemandCreateRequest;
import com.cyan.datacollection.adapter.demand.controller.request.TrackingDemandUpdateRequest;
import com.cyan.datacollection.domain.demand.query.TrackingDemandPageQuery;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 埋点需求适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingDemandAdapterConvert {

    TrackingDemandAdapterConvert INSTANCE = Mappers.getMapper(TrackingDemandAdapterConvert.class);

    TrackingDemandDTO toClientDTO(TrackingDemandBO bo);

    List<TrackingDemandDTO> toClientDTOList(List<TrackingDemandBO> bos);

    TrackingDemandCmd toCmd(TrackingDemandCreateRequest request);

    TrackingDemandCmd toCmd(TrackingDemandUpdateRequest request);

    TrackingDemandPageQuery toPageQuery(com.cyan.datacollection.adapter.demand.controller.request.TrackingDemandPageQuery query);
}

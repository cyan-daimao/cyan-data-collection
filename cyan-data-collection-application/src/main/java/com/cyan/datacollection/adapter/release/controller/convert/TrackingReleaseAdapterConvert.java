package com.cyan.datacollection.adapter.release.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.adapter.release.controller.dto.TrackingReleaseDTO;
import com.cyan.datacollection.adapter.release.controller.dto.TrackingReleaseItemDTO;
import com.cyan.datacollection.adapter.release.controller.request.TrackingReleaseCreateRequest;
import com.cyan.datacollection.application.release.bo.TrackingReleaseBO;
import com.cyan.datacollection.application.release.cmd.TrackingReleaseCmd;
import com.cyan.datacollection.domain.release.query.TrackingReleasePageQuery;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 埋点发布版本适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingReleaseAdapterConvert {

    TrackingReleaseAdapterConvert INSTANCE = Mappers.getMapper(TrackingReleaseAdapterConvert.class);

    TrackingReleaseDTO toClientDTO(TrackingReleaseBO bo);

    TrackingReleaseItemDTO toItemDTO(TrackingReleaseBO.ItemBO itemBO);

    TrackingReleaseCmd toCmd(TrackingReleaseCreateRequest request);

    TrackingReleasePageQuery toPageQuery(com.cyan.datacollection.adapter.release.controller.request.TrackingReleasePageQuery query);
}

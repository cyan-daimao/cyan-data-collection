package com.cyan.datacollection.adapter.debug.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.debug.bo.DebugEventSampleBO;
import com.cyan.datacollection.application.debug.bo.DebugSessionBO;
import com.cyan.datacollection.application.debug.cmd.DebugSessionCmd;
import com.cyan.datacollection.adapter.debug.controller.dto.DebugEventSampleDTO;
import com.cyan.datacollection.adapter.debug.controller.dto.DebugSessionDTO;
import com.cyan.datacollection.adapter.debug.controller.request.DebugEventPageQuery;
import com.cyan.datacollection.adapter.debug.controller.request.DebugSessionCreateRequest;
import com.cyan.datacollection.domain.collect.query.TrackingEventSamplePageQuery;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Debug 控制台适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingDebugAdapterConvert {

    TrackingDebugAdapterConvert INSTANCE = Mappers.getMapper(TrackingDebugAdapterConvert.class);

    DebugSessionDTO toClientSessionDTO(DebugSessionBO bo);

    DebugEventSampleDTO toClientEventSampleDTO(DebugEventSampleBO bo);

    DebugSessionCmd toCmd(DebugSessionCreateRequest request);

    TrackingEventSamplePageQuery toPageQuery(DebugEventPageQuery query);
}

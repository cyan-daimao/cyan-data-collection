package com.cyan.datacollection.application.debug.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.debug.bo.DebugEventSampleBO;
import com.cyan.datacollection.application.debug.bo.DebugSessionBO;
import com.cyan.datacollection.domain.collect.TrackingEventSample;
import com.cyan.datacollection.domain.debug.TrackingDebugSession;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Debug 控制台应用层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingDebugAppConvert {

    TrackingDebugAppConvert INSTANCE = Mappers.getMapper(TrackingDebugAppConvert.class);

    DebugSessionBO toDebugSessionBO(TrackingDebugSession session);

    DebugEventSampleBO toDebugEventSampleBO(TrackingEventSample sample);
}

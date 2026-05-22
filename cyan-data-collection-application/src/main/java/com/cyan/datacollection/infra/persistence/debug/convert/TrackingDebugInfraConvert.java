package com.cyan.datacollection.infra.persistence.debug.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.debug.TrackingDebugSession;
import com.cyan.datacollection.infra.persistence.debug.dos.TrackingDebugSessionDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Debug 会话转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingDebugInfraConvert {

    TrackingDebugInfraConvert INSTANCE = Mappers.getMapper(TrackingDebugInfraConvert.class);

    TrackingDebugSession toTrackingDebugSession(TrackingDebugSessionDO trackingDebugSessionDO);

    TrackingDebugSessionDO toTrackingDebugSessionDO(TrackingDebugSession trackingDebugSession);
}

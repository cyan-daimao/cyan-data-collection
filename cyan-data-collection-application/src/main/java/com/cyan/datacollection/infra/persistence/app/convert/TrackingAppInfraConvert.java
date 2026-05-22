package com.cyan.datacollection.infra.persistence.app.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.app.TrackingApp;
import com.cyan.datacollection.infra.persistence.app.dos.TrackingAppDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 接入应用转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingAppInfraConvert {

    TrackingAppInfraConvert INSTANCE = Mappers.getMapper(TrackingAppInfraConvert.class);

    TrackingApp toTrackingApp(TrackingAppDO trackingAppDO);

    TrackingAppDO toTrackingAppDO(TrackingApp trackingApp);
}

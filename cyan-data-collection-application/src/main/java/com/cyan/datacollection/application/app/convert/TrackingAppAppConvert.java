package com.cyan.datacollection.application.app.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.app.bo.TrackingAppBO;
import com.cyan.datacollection.application.app.cmd.TrackingAppCmd;
import com.cyan.datacollection.domain.app.TrackingApp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 接入应用应用层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingAppAppConvert {

    TrackingAppAppConvert INSTANCE = Mappers.getMapper(TrackingAppAppConvert.class);

    TrackingAppBO toTrackingAppBO(TrackingApp trackingApp);

    TrackingApp toTrackingApp(TrackingAppCmd cmd);
}

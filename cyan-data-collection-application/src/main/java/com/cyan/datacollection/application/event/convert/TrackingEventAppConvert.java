package com.cyan.datacollection.application.event.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.event.bo.TrackingEventBO;
import com.cyan.datacollection.application.event.cmd.TrackingEventCmd;
import com.cyan.datacollection.domain.event.TrackingEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 事件定义应用层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingEventAppConvert {

    TrackingEventAppConvert INSTANCE = Mappers.getMapper(TrackingEventAppConvert.class);

    TrackingEventBO toTrackingEventBO(TrackingEvent trackingEvent);

    TrackingEvent toTrackingEvent(TrackingEventCmd cmd);
}

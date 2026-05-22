package com.cyan.datacollection.infra.persistence.event.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.event.TrackingEvent;
import com.cyan.datacollection.infra.persistence.event.dos.TrackingEventDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 事件定义转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingEventInfraConvert {

    TrackingEventInfraConvert INSTANCE = Mappers.getMapper(TrackingEventInfraConvert.class);

    TrackingEvent toTrackingEvent(TrackingEventDO trackingEventDO);

    TrackingEventDO toTrackingEventDO(TrackingEvent trackingEvent);

    default List<String> toTerminalTypes(String terminalTypes) {
        if (terminalTypes == null || terminalTypes.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(terminalTypes.split(","));
    }

    default String toTerminalTypesString(List<String> terminalTypes) {
        if (terminalTypes == null || terminalTypes.isEmpty()) {
            return null;
        }
        return String.join(",", terminalTypes);
    }
}

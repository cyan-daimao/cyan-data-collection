package com.cyan.datacollection.infra.persistence.demand.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.demand.TrackingDemand;
import com.cyan.datacollection.infra.persistence.demand.dos.TrackingDemandDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 埋点需求转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingDemandInfraConvert {

    TrackingDemandInfraConvert INSTANCE = Mappers.getMapper(TrackingDemandInfraConvert.class);

    TrackingDemand toTrackingDemand(TrackingDemandDO trackingDemandDO);

    TrackingDemandDO toTrackingDemandDO(TrackingDemand trackingDemand);

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

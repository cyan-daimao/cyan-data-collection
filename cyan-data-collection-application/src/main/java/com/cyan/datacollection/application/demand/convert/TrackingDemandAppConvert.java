package com.cyan.datacollection.application.demand.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.demand.bo.TrackingDemandBO;
import com.cyan.datacollection.application.demand.cmd.TrackingDemandCmd;
import com.cyan.datacollection.domain.demand.TrackingDemand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 埋点需求应用层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingDemandAppConvert {

    TrackingDemandAppConvert INSTANCE = Mappers.getMapper(TrackingDemandAppConvert.class);

    TrackingDemandBO toTrackingDemandBO(TrackingDemand trackingDemand);

    TrackingDemand toTrackingDemand(TrackingDemandCmd cmd);

    default TrackingDemandBO toBO(TrackingDemand trackingDemand) {
        return toTrackingDemandBO(trackingDemand);
    }

    default TrackingDemand toDomain(TrackingDemandCmd cmd) {
        return toTrackingDemand(cmd);
    }
}

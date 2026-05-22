package com.cyan.datacollection.infra.persistence.eventproperty.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.eventproperty.TrackingEventProperty;
import com.cyan.datacollection.infra.persistence.eventproperty.dos.TrackingEventPropertyDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 事件属性关系转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingEventPropertyInfraConvert {

    TrackingEventPropertyInfraConvert INSTANCE = Mappers.getMapper(TrackingEventPropertyInfraConvert.class);

    TrackingEventProperty toTrackingEventProperty(TrackingEventPropertyDO trackingEventPropertyDO);

    TrackingEventPropertyDO toTrackingEventPropertyDO(TrackingEventProperty trackingEventProperty);
}

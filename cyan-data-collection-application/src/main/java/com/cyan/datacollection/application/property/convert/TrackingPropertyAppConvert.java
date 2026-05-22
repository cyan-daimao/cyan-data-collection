package com.cyan.datacollection.application.property.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.property.bo.TrackingPropertyBO;
import com.cyan.datacollection.application.property.cmd.TrackingPropertyCmd;
import com.cyan.datacollection.domain.property.TrackingProperty;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 属性定义应用层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingPropertyAppConvert {

    TrackingPropertyAppConvert INSTANCE = Mappers.getMapper(TrackingPropertyAppConvert.class);

    TrackingPropertyBO toTrackingPropertyBO(TrackingProperty trackingProperty);

    TrackingProperty toTrackingProperty(TrackingPropertyCmd cmd);
}

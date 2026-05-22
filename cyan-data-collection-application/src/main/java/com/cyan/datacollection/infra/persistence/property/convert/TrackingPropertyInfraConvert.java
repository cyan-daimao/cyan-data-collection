package com.cyan.datacollection.infra.persistence.property.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.property.TrackingProperty;
import com.cyan.datacollection.infra.persistence.property.dos.TrackingPropertyDO;
import com.alibaba.fastjson2.JSON;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 属性定义转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingPropertyInfraConvert {

    TrackingPropertyInfraConvert INSTANCE = Mappers.getMapper(TrackingPropertyInfraConvert.class);

    TrackingProperty toTrackingProperty(TrackingPropertyDO trackingPropertyDO);

    TrackingPropertyDO toTrackingPropertyDO(TrackingProperty trackingProperty);

    default List<String> toEnumValues(String enumValues) {
        if (enumValues == null || enumValues.isEmpty()) {
            return null;
        }
        return JSON.parseArray(enumValues, String.class);
    }

    default String toEnumValuesString(List<String> enumValues) {
        if (enumValues == null || enumValues.isEmpty()) {
            return null;
        }
        return JSON.toJSONString(enumValues);
    }
}

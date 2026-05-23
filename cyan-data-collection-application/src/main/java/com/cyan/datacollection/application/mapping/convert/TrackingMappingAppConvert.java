package com.cyan.datacollection.application.mapping.convert;

import com.cyan.datacollection.application.mapping.bo.EventMetricMappingBO;
import com.cyan.datacollection.application.mapping.bo.PropertyDimensionMappingBO;
import com.cyan.datacollection.domain.mapping.TrackingEventMetricMapping;
import com.cyan.datacollection.domain.mapping.TrackingPropertyDimensionMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 采集映射应用层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingMappingAppConvert {

    /**
     * 转换实例
     */
    TrackingMappingAppConvert INSTANCE = Mappers.getMapper(TrackingMappingAppConvert.class);

    /**
     * 转换属性维度映射业务对象
     */
    PropertyDimensionMappingBO toPropertyDimensionMappingBO(TrackingPropertyDimensionMapping mapping);

    /**
     * 转换事件指标映射业务对象
     */
    EventMetricMappingBO toEventMetricMappingBO(TrackingEventMetricMapping mapping);
}

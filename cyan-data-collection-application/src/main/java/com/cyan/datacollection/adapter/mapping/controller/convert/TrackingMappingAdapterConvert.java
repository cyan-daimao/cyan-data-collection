package com.cyan.datacollection.adapter.mapping.controller.convert;

import com.cyan.datacollection.adapter.mapping.controller.dto.EventMetricMappingDTO;
import com.cyan.datacollection.adapter.mapping.controller.dto.PropertyDimensionMappingDTO;
import com.cyan.datacollection.adapter.mapping.controller.request.EventMetricSyncRequest;
import com.cyan.datacollection.adapter.mapping.controller.request.PropertyDimensionSyncRequest;
import com.cyan.datacollection.application.mapping.bo.EventMetricMappingBO;
import com.cyan.datacollection.application.mapping.bo.PropertyDimensionMappingBO;
import com.cyan.datacollection.application.mapping.cmd.EventMetricSyncCmd;
import com.cyan.datacollection.application.mapping.cmd.PropertyDimensionSyncCmd;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 采集映射适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingMappingAdapterConvert {

    /**
     * 转换实例
     */
    TrackingMappingAdapterConvert INSTANCE = Mappers.getMapper(TrackingMappingAdapterConvert.class);

    /**
     * 转换属性同步命令
     */
    PropertyDimensionSyncCmd toCmd(PropertyDimensionSyncRequest request);

    /**
     * 转换事件同步命令
     */
    EventMetricSyncCmd toCmd(EventMetricSyncRequest request);

    /**
     * 转换属性维度映射DTO
     */
    PropertyDimensionMappingDTO toDTO(PropertyDimensionMappingBO bo);

    /**
     * 转换事件指标映射DTO
     */
    EventMetricMappingDTO toDTO(EventMetricMappingBO bo);
}

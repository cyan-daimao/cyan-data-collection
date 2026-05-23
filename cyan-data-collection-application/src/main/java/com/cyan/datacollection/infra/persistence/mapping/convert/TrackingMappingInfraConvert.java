package com.cyan.datacollection.infra.persistence.mapping.convert;

import com.cyan.datacollection.domain.mapping.TrackingEventMetricMapping;
import com.cyan.datacollection.domain.mapping.TrackingPropertyDimensionMapping;
import com.cyan.datacollection.infra.persistence.mapping.dos.TrackingEventMetricMappingDO;
import com.cyan.datacollection.infra.persistence.mapping.dos.TrackingPropertyDimensionMappingDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 采集映射基础设施层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingMappingInfraConvert {

    /**
     * 转换实例
     */
    TrackingMappingInfraConvert INSTANCE = Mappers.getMapper(TrackingMappingInfraConvert.class);

    /**
     * DO转属性维度映射
     */
    default TrackingPropertyDimensionMapping toPropertyDimensionMapping(TrackingPropertyDimensionMappingDO source) {
        if (source == null) {
            return null;
        }
        return new TrackingPropertyDimensionMapping()
                .setId(source.getId() == null ? null : String.valueOf(source.getId()))
                .setPropertyId(source.getPropertyId() == null ? null : String.valueOf(source.getPropertyId()))
                .setPropertyCode(source.getPropertyCode())
                .setDimId(source.getDimId())
                .setDimCode(source.getDimCode())
                .setSyncStatus(source.getSyncStatus())
                .setErrorMessage(source.getErrorMessage())
                .setCreateBy(source.getCreateBy())
                .setUpdateBy(source.getUpdateBy())
                .setCreatedAt(source.getCreatedAt())
                .setUpdatedAt(source.getUpdatedAt())
                .setDeletedAt(source.getDeletedAt());
    }

    /**
     * 属性维度映射转DO
     */
    default TrackingPropertyDimensionMappingDO toPropertyDimensionMappingDO(TrackingPropertyDimensionMapping source) {
        if (source == null) {
            return null;
        }
        return new TrackingPropertyDimensionMappingDO()
                .setId(source.getId() == null ? null : Long.parseLong(source.getId()))
                .setPropertyId(source.getPropertyId() == null ? null : Long.parseLong(source.getPropertyId()))
                .setPropertyCode(source.getPropertyCode())
                .setDimId(source.getDimId())
                .setDimCode(source.getDimCode())
                .setSyncStatus(source.getSyncStatus())
                .setErrorMessage(source.getErrorMessage())
                .setCreateBy(source.getCreateBy())
                .setUpdateBy(source.getUpdateBy())
                .setCreatedAt(source.getCreatedAt())
                .setUpdatedAt(source.getUpdatedAt());
    }

    /**
     * DO转事件指标映射
     */
    default TrackingEventMetricMapping toEventMetricMapping(TrackingEventMetricMappingDO source) {
        if (source == null) {
            return null;
        }
        return new TrackingEventMetricMapping()
                .setId(source.getId() == null ? null : String.valueOf(source.getId()))
                .setEventId(source.getEventId() == null ? null : String.valueOf(source.getEventId()))
                .setEventCode(source.getEventCode())
                .setMetricId(source.getMetricId())
                .setMetricCode(source.getMetricCode())
                .setSyncStatus(source.getSyncStatus())
                .setErrorMessage(source.getErrorMessage())
                .setCreateBy(source.getCreateBy())
                .setUpdateBy(source.getUpdateBy())
                .setCreatedAt(source.getCreatedAt())
                .setUpdatedAt(source.getUpdatedAt())
                .setDeletedAt(source.getDeletedAt());
    }

    /**
     * 事件指标映射转DO
     */
    default TrackingEventMetricMappingDO toEventMetricMappingDO(TrackingEventMetricMapping source) {
        if (source == null) {
            return null;
        }
        return new TrackingEventMetricMappingDO()
                .setId(source.getId() == null ? null : Long.parseLong(source.getId()))
                .setEventId(source.getEventId() == null ? null : Long.parseLong(source.getEventId()))
                .setEventCode(source.getEventCode())
                .setMetricId(source.getMetricId())
                .setMetricCode(source.getMetricCode())
                .setSyncStatus(source.getSyncStatus())
                .setErrorMessage(source.getErrorMessage())
                .setCreateBy(source.getCreateBy())
                .setUpdateBy(source.getUpdateBy())
                .setCreatedAt(source.getCreatedAt())
                .setUpdatedAt(source.getUpdatedAt());
    }
}

package com.cyan.datacollection.adapter.metric.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.adapter.metric.controller.dto.TrackingMetricPipelineDTO;
import com.cyan.datacollection.application.metric.bo.TrackingMetricPipelineBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 采集指标链路适配器层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingMetricPipelineAdapterConvert {

    TrackingMetricPipelineAdapterConvert INSTANCE = Mappers.getMapper(TrackingMetricPipelineAdapterConvert.class);

    TrackingMetricPipelineDTO toTrackingMetricPipelineDTO(TrackingMetricPipelineBO bo);
}

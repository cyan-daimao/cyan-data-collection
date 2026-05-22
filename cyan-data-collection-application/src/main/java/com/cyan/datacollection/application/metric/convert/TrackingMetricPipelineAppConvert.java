package com.cyan.datacollection.application.metric.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.metric.bo.TrackingMetricPipelineBO;
import com.cyan.datacollection.application.metric.cmd.TrackingMetricPipelineCmd;
import com.cyan.datacollection.domain.metric.TrackingMetricPipeline;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 采集指标链路应用层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingMetricPipelineAppConvert {

    TrackingMetricPipelineAppConvert INSTANCE = Mappers.getMapper(TrackingMetricPipelineAppConvert.class);

    TrackingMetricPipeline toTrackingMetricPipeline(TrackingMetricPipelineCmd cmd);

    TrackingMetricPipelineBO toTrackingMetricPipelineBO(TrackingMetricPipeline pipeline);
}

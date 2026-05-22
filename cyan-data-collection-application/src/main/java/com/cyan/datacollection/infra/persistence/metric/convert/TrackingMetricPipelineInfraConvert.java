package com.cyan.datacollection.infra.persistence.metric.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.metric.TrackingMetricPipeline;
import com.cyan.datacollection.infra.persistence.metric.dos.TrackingMetricPipelineDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 采集指标链路转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingMetricPipelineInfraConvert {

    TrackingMetricPipelineInfraConvert INSTANCE = Mappers.getMapper(TrackingMetricPipelineInfraConvert.class);

    TrackingMetricPipeline toTrackingMetricPipeline(TrackingMetricPipelineDO trackingMetricPipelineDO);

    TrackingMetricPipelineDO toTrackingMetricPipelineDO(TrackingMetricPipeline trackingMetricPipeline);
}

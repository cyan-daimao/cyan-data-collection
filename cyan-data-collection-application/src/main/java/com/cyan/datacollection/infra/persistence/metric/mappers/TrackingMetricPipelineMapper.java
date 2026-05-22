package com.cyan.datacollection.infra.persistence.metric.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.metric.dos.TrackingMetricPipelineDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采集指标链路 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingMetricPipelineMapper extends BaseMapper<TrackingMetricPipelineDO> {
}

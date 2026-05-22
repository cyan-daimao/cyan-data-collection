package com.cyan.datacollection.infra.persistence.quality.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.quality.dos.TrackingQualityMetricDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 质量指标 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingQualityMetricMapper extends BaseMapper<TrackingQualityMetricDO> {
}

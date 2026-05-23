package com.cyan.datacollection.infra.persistence.mapping.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.mapping.dos.TrackingEventMetricMappingDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采集事件指标映射Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingEventMetricMappingMapper extends BaseMapper<TrackingEventMetricMappingDO> {
}

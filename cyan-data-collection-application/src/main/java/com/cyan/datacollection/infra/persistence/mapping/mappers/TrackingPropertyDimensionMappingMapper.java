package com.cyan.datacollection.infra.persistence.mapping.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.mapping.dos.TrackingPropertyDimensionMappingDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采集属性维度映射Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingPropertyDimensionMappingMapper extends BaseMapper<TrackingPropertyDimensionMappingDO> {
}

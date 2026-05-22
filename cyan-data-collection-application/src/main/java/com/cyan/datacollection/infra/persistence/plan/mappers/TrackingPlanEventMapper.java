package com.cyan.datacollection.infra.persistence.plan.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.plan.dos.TrackingPlanEventDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 方案事件关系 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingPlanEventMapper extends BaseMapper<TrackingPlanEventDO> {
}

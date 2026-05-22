package com.cyan.datacollection.infra.persistence.event.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.event.dos.TrackingEventDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 事件定义 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingEventMapper extends BaseMapper<TrackingEventDO> {
}

package com.cyan.datacollection.infra.persistence.eventproperty.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.eventproperty.dos.TrackingEventPropertyDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 事件属性关系 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingEventPropertyMapper extends BaseMapper<TrackingEventPropertyDO> {
}

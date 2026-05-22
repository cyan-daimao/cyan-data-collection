package com.cyan.datacollection.infra.persistence.collect.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.collect.dos.TrackingEventSampleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 事件样本 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingEventSampleMapper extends BaseMapper<TrackingEventSampleDO> {
}

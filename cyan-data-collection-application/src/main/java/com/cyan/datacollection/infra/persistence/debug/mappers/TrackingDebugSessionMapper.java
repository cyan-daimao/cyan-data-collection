package com.cyan.datacollection.infra.persistence.debug.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.debug.dos.TrackingDebugSessionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Debug 会话 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingDebugSessionMapper extends BaseMapper<TrackingDebugSessionDO> {
}

package com.cyan.datacollection.infra.persistence.app.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.app.dos.TrackingAppDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 接入应用 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingAppMapper extends BaseMapper<TrackingAppDO> {
}

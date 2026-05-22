package com.cyan.datacollection.infra.persistence.quality.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.quality.dos.TrackingAlertDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 质量告警 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingAlertMapper extends BaseMapper<TrackingAlertDO> {
}

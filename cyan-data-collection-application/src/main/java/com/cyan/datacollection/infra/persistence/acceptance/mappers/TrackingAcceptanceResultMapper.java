package com.cyan.datacollection.infra.persistence.acceptance.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.acceptance.dos.TrackingAcceptanceResultDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 埋点验收结果 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingAcceptanceResultMapper extends BaseMapper<TrackingAcceptanceResultDO> {
}

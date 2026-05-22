package com.cyan.datacollection.infra.persistence.release.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.release.dos.TrackingReleaseItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 埋点发布版本明细 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingReleaseItemMapper extends BaseMapper<TrackingReleaseItemDO> {
}

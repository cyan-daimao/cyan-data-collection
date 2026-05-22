package com.cyan.datacollection.infra.persistence.property.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.property.dos.TrackingPropertyDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 属性定义 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingPropertyMapper extends BaseMapper<TrackingPropertyDO> {
}

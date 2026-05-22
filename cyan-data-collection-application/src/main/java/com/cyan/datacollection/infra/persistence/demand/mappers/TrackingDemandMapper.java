package com.cyan.datacollection.infra.persistence.demand.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.demand.dos.TrackingDemandDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 埋点需求 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingDemandMapper extends BaseMapper<TrackingDemandDO> {

    /**
     * 获取当天最大序号
     */
    @Select("SELECT IFNULL(MAX(CAST(SUBSTRING(demand_code, 11) AS UNSIGNED)), 0) FROM tracking_demand WHERE demand_code LIKE CONCAT('TD', DATE_FORMAT(NOW(), '%Y%m%d'), '%') AND deleted_at IS NULL")
    int findMaxSeqToday();
}

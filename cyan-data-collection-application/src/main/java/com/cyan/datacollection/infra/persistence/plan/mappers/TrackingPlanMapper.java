package com.cyan.datacollection.infra.persistence.plan.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.plan.dos.TrackingPlanDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 埋点方案 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingPlanMapper extends BaseMapper<TrackingPlanDO> {

    /**
     * 获取当天最大序号
     */
    @Select("SELECT IFNULL(MAX(CAST(SUBSTRING(plan_code, 11) AS UNSIGNED)), 0) FROM tracking_plan WHERE plan_code LIKE CONCAT('TP', DATE_FORMAT(NOW(), '%Y%m%d'), '%') AND deleted_at IS NULL")
    int findMaxSeqToday();
}

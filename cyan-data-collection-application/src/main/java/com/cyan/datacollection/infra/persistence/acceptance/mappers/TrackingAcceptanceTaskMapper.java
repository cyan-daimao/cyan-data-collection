package com.cyan.datacollection.infra.persistence.acceptance.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.acceptance.dos.TrackingAcceptanceTaskDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 埋点验收任务 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingAcceptanceTaskMapper extends BaseMapper<TrackingAcceptanceTaskDO> {

    /**
     * 获取当天最大序号
     */
    @Select("SELECT IFNULL(MAX(CAST(SUBSTRING(task_code, 10) AS UNSIGNED)), 0) FROM tracking_acceptance_task WHERE task_code LIKE CONCAT('AT', DATE_FORMAT(NOW(), '%Y%m%d'), '%') AND deleted_at IS NULL")
    int findMaxSeqToday();
}

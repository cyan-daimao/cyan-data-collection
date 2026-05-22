package com.cyan.datacollection.infra.persistence.workbench.mappers;

import com.cyan.datacollection.application.workbench.bo.WorkbenchTodoBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 工作台聚合查询 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingWorkbenchMapper {

    /**
     * 事件总数
     */
    @Select("SELECT COUNT(*) FROM tracking_event WHERE deleted_at IS NULL")
    Long countEvents();

    /**
     * 属性总数
     */
    @Select("SELECT COUNT(*) FROM tracking_property WHERE deleted_at IS NULL")
    Long countProperties();

    /**
     * 方案总数
     */
    @Select("SELECT COUNT(*) FROM tracking_plan WHERE deleted_at IS NULL")
    Long countPlans();

    /**
     * 今日上报量
     */
    @Select("SELECT COUNT(*) FROM tracking_event_sample WHERE deleted_at IS NULL AND DATE(created_at) = CURDATE()")
    Long countTodaySamples();

    /**
     * 今日失败样本数
     */
    @Select("SELECT COUNT(*) FROM tracking_event_sample WHERE deleted_at IS NULL AND DATE(created_at) = CURDATE() AND validate_status = 'FAIL'")
    Long countTodayFailSamples();

    /**
     * 待评审方案数
     */
    @Select("SELECT COUNT(*) FROM tracking_plan WHERE deleted_at IS NULL AND status = 'REVIEWING'")
    Long countReviewingPlans();

    /**
     * 待验收任务数
     */
    @Select("SELECT COUNT(*) FROM tracking_acceptance_task WHERE deleted_at IS NULL AND status = 'PENDING'")
    Long countPendingTasks();

    /**
     * 待评审方案列表
     */
    @Select("SELECT id as id, plan_code as code, plan_name as name, status as status, created_at as createdAt "
            + "FROM tracking_plan WHERE deleted_at IS NULL AND status = 'REVIEWING' ORDER BY created_at DESC LIMIT 20")
    List<WorkbenchTodoBO> selectReviewingPlans();

    /**
     * 待验收任务列表
     */
    @Select("SELECT id as id, task_code as code, plan_id as name, status as status, created_at as createdAt "
            + "FROM tracking_acceptance_task WHERE deleted_at IS NULL AND status = 'PENDING' ORDER BY created_at DESC LIMIT 20")
    List<WorkbenchTodoBO> selectPendingTasks();
}

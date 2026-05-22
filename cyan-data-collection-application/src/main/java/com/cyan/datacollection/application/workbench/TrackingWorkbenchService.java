package com.cyan.datacollection.application.workbench;

import com.cyan.datacollection.application.workbench.bo.WorkbenchQualityRiskBO;
import com.cyan.datacollection.application.workbench.bo.WorkbenchSummaryBO;
import com.cyan.datacollection.application.workbench.bo.WorkbenchTodoBO;

import java.util.List;

/**
 * 工作台服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingWorkbenchService {

    /**
     * 统计卡片
     */
    WorkbenchSummaryBO summary();

    /**
     * 待办列表
     */
    List<WorkbenchTodoBO> todos();

    /**
     * 质量风险列表
     */
    List<WorkbenchQualityRiskBO> qualityRisks();
}

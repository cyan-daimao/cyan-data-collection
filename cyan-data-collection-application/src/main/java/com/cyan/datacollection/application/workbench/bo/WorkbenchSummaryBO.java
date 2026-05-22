package com.cyan.datacollection.application.workbench.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 工作台统计卡片业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WorkbenchSummaryBO {

    /**
     * 事件总数
     */
    private Long eventCount;

    /**
     * 属性总数
     */
    private Long propertyCount;

    /**
     * 方案总数
     */
    private Long planCount;

    /**
     * 今日上报量
     */
    private Long todaySampleCount;

    /**
     * 今日失败样本数
     */
    private Long todayFailSampleCount;

    /**
     * 待评审方案数
     */
    private Long reviewingPlanCount;

    /**
     * 待验收任务数
     */
    private Long pendingTaskCount;

    /**
     * 未关闭告警数
     */
    private Long openAlertCount;
}

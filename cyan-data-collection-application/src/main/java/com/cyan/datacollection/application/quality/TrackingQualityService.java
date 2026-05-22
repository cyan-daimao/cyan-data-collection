package com.cyan.datacollection.application.quality;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.application.quality.bo.QualityOverviewBO;
import com.cyan.datacollection.application.quality.bo.QualityTrendBO;
import com.cyan.datacollection.application.quality.cmd.QualityOverviewQuery;
import com.cyan.datacollection.application.quality.cmd.QualityTrendQuery;
import com.cyan.datacollection.domain.quality.query.TrackingAlertPageQuery;
import com.cyan.datacollection.domain.quality.TrackingAlert;

import java.util.List;

/**
 * 质量监控服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingQualityService {

    /**
     * 事件质量总览
     */
    List<QualityOverviewBO> eventsOverview(QualityOverviewQuery query);

    /**
     * 事件质量趋势
     */
    List<QualityTrendBO> eventsTrend(QualityTrendQuery query);

    /**
     * 告警分页查询
     */
    Page<TrackingAlert> alertPage(TrackingAlertPageQuery query);

    /**
     * 关闭告警
     */
    TrackingAlert closeAlert(String id);
}

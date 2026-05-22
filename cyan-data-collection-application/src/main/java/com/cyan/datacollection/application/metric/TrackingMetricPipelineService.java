package com.cyan.datacollection.application.metric;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.application.metric.bo.TrackingMetricPipelineBO;
import com.cyan.datacollection.application.metric.cmd.TrackingMetricPipelineCmd;
import com.cyan.datacollection.domain.metric.query.TrackingMetricPipelinePageQuery;

/**
 * 采集指标链路服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingMetricPipelineService {

    /**
     * 分页查询
     */
    Page<TrackingMetricPipelineBO> page(TrackingMetricPipelinePageQuery query);

    /**
     * 创建
     */
    TrackingMetricPipelineBO create(TrackingMetricPipelineCmd cmd, String createdBy);

    /**
     * 详情
     */
    TrackingMetricPipelineBO detail(String id);

    /**
     * 创建表和链路（provision）
     */
    TrackingMetricPipelineBO provision(String id);

    /**
     * 启动任务（start）
     */
    TrackingMetricPipelineBO start(String id);
}

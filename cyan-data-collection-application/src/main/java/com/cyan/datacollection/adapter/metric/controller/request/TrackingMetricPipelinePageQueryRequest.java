package com.cyan.datacollection.adapter.metric.controller.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * 采集指标链路分页查询请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingMetricPipelinePageQueryRequest {

    @JsonAlias({"pageNo", "current"})
    private long pageNum = 1;
    @JsonAlias("size")
    private long pageSize = 20;
    private String metricCode;
    private String metricName;
    private String status;
}

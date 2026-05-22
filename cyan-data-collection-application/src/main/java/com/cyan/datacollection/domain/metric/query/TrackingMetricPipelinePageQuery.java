package com.cyan.datacollection.domain.metric.query;

import com.cyan.arch.common.api.Pageable;
import lombok.Data;

/**
 * 采集指标链路分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingMetricPipelinePageQuery implements Pageable {

    private long pageNum = 1;
    private long pageSize = 20;
    private String metricCode;
    private String metricName;
    private String status;

    @Override
    public long current() {
        return pageNum;
    }

    @Override
    public long size() {
        return pageSize;
    }
}

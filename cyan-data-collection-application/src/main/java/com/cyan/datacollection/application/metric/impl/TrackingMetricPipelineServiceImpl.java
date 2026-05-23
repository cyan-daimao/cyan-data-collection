package com.cyan.datacollection.application.metric.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.metric.TrackingMetricPipelineService;
import com.cyan.datacollection.application.metric.bo.TrackingMetricPipelineBO;
import com.cyan.datacollection.application.metric.cmd.TrackingMetricPipelineCmd;
import com.cyan.datacollection.application.metric.convert.TrackingMetricPipelineAppConvert;
import com.cyan.datacollection.domain.metric.TrackingMetricPipeline;
import com.cyan.datacollection.domain.metric.query.TrackingMetricPipelinePageQuery;
import com.cyan.datacollection.domain.metric.repository.TrackingMetricPipelineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 采集指标链路服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingMetricPipelineServiceImpl implements TrackingMetricPipelineService {

    private static final String DEPRECATED_MESSAGE = "采集指标链路已废弃，请使用属性转维度、事件转指标能力";

    private final TrackingMetricPipelineRepository repository;

    public TrackingMetricPipelineServiceImpl(TrackingMetricPipelineRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<TrackingMetricPipelineBO> page(TrackingMetricPipelinePageQuery query) {
        Page<TrackingMetricPipeline> page = repository.page(query);
        List<TrackingMetricPipelineBO> list = page.getData().stream()
                .map(TrackingMetricPipelineAppConvert.INSTANCE::toTrackingMetricPipelineBO)
                .toList();
        return new Page<>(list, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    public TrackingMetricPipelineBO create(TrackingMetricPipelineCmd cmd, String createdBy) {
        throw new SilentException(DEPRECATED_MESSAGE);
    }

    @Override
    public TrackingMetricPipelineBO detail(String id) {
        TrackingMetricPipeline pipeline = repository.findById(id);
        Assert.notNull(pipeline, new SilentException("采集指标链路不存在"));
        return TrackingMetricPipelineAppConvert.INSTANCE.toTrackingMetricPipelineBO(pipeline);
    }

    @Override
    public TrackingMetricPipelineBO provision(String id) {
        TrackingMetricPipeline pipeline = repository.findById(id);
        Assert.notNull(pipeline, new SilentException("采集指标链路不存在"));
        throw new SilentException(DEPRECATED_MESSAGE);
    }

    @Override
    public TrackingMetricPipelineBO start(String id) {
        TrackingMetricPipeline pipeline = repository.findById(id);
        Assert.notNull(pipeline, new SilentException("采集指标链路不存在"));
        throw new SilentException(DEPRECATED_MESSAGE);
    }
}

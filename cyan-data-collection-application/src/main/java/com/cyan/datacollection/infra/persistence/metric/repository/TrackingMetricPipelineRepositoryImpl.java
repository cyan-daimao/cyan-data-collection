package com.cyan.datacollection.infra.persistence.metric.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyan.arch.common.api.Pageable;
import com.cyan.datacollection.domain.metric.TrackingMetricPipeline;
import com.cyan.datacollection.domain.metric.query.TrackingMetricPipelinePageQuery;
import com.cyan.datacollection.domain.metric.repository.TrackingMetricPipelineRepository;
import com.cyan.datacollection.enums.MetricPipelineStatus;
import com.cyan.datacollection.infra.persistence.metric.convert.TrackingMetricPipelineInfraConvert;
import com.cyan.datacollection.infra.persistence.metric.dos.TrackingMetricPipelineDO;
import com.cyan.datacollection.infra.persistence.metric.mappers.TrackingMetricPipelineMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 采集指标链路仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingMetricPipelineRepositoryImpl implements TrackingMetricPipelineRepository {

    private final TrackingMetricPipelineMapper mapper;

    public TrackingMetricPipelineRepositoryImpl(TrackingMetricPipelineMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public TrackingMetricPipeline findById(String id) {
        TrackingMetricPipelineDO trackingMetricPipelineDO = mapper.selectById(Long.parseLong(id));
        return TrackingMetricPipelineInfraConvert.INSTANCE.toTrackingMetricPipeline(trackingMetricPipelineDO);
    }

    @Override
    public TrackingMetricPipeline findByMetricCode(String metricCode) {
        LambdaQueryWrapper<TrackingMetricPipelineDO> wrapper = new LambdaQueryWrapper<TrackingMetricPipelineDO>()
                .eq(TrackingMetricPipelineDO::getMetricCode, metricCode);
        TrackingMetricPipelineDO trackingMetricPipelineDO = mapper.selectOne(wrapper);
        return TrackingMetricPipelineInfraConvert.INSTANCE.toTrackingMetricPipeline(trackingMetricPipelineDO);
    }

    @Override
    public com.cyan.arch.common.api.Page<TrackingMetricPipeline> page(TrackingMetricPipelinePageQuery query) {
        Page<TrackingMetricPipelineDO> page = new Page<>(query.current(), query.size());
        boolean hasMetricCode = StringUtils.isNotBlank(query.getMetricCode());
        boolean hasMetricName = StringUtils.isNotBlank(query.getMetricName());
        boolean hasStatus = StringUtils.isNotBlank(query.getStatus());
        LambdaQueryWrapper<TrackingMetricPipelineDO> wrapper = new LambdaQueryWrapper<TrackingMetricPipelineDO>()
                .like(hasMetricCode, TrackingMetricPipelineDO::getMetricCode, query.getMetricCode())
                .like(hasMetricName, TrackingMetricPipelineDO::getMetricName, query.getMetricName())
                .eq(hasStatus, TrackingMetricPipelineDO::getStatus, MetricPipelineStatus.of(query.getStatus()))
                .orderByDesc(TrackingMetricPipelineDO::getUpdatedAt);
        Page<TrackingMetricPipelineDO> result = mapper.selectPage(page, wrapper);
        List<TrackingMetricPipeline> list = Optional.ofNullable(result.getRecords()).orElse(List.of()).stream()
                .map(TrackingMetricPipelineInfraConvert.INSTANCE::toTrackingMetricPipeline)
                .toList();
        return new com.cyan.arch.common.api.Page<>(list, result.getCurrent(), result.getSize(), result.getTotal());
    }

    @Override
    public TrackingMetricPipeline save(TrackingMetricPipeline pipeline) {
        TrackingMetricPipelineDO trackingMetricPipelineDO = TrackingMetricPipelineInfraConvert.INSTANCE.toTrackingMetricPipelineDO(pipeline);
        mapper.insert(trackingMetricPipelineDO);
        return findById(String.valueOf(trackingMetricPipelineDO.getId()));
    }

    @Override
    public TrackingMetricPipeline update(TrackingMetricPipeline pipeline) {
        TrackingMetricPipelineDO trackingMetricPipelineDO = TrackingMetricPipelineInfraConvert.INSTANCE.toTrackingMetricPipelineDO(pipeline);
        trackingMetricPipelineDO.setId(Long.parseLong(pipeline.getId()));
        mapper.updateById(trackingMetricPipelineDO);
        return findById(pipeline.getId());
    }

    @Override
    public void deleteById(String id) {
        mapper.deleteById(Long.parseLong(id));
    }
}

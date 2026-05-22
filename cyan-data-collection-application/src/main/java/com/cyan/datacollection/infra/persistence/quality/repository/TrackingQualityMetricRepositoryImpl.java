package com.cyan.datacollection.infra.persistence.quality.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyan.arch.common.api.Pageable;
import com.cyan.datacollection.domain.quality.TrackingQualityMetric;
import com.cyan.datacollection.domain.quality.query.TrackingQualityMetricPageQuery;
import com.cyan.datacollection.domain.quality.repository.TrackingQualityMetricRepository;
import com.cyan.datacollection.infra.persistence.quality.convert.TrackingQualityInfraConvert;
import com.cyan.datacollection.infra.persistence.quality.dos.TrackingQualityMetricDO;
import com.cyan.datacollection.infra.persistence.quality.mappers.TrackingQualityMetricMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 质量指标仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingQualityMetricRepositoryImpl implements TrackingQualityMetricRepository {

    private final TrackingQualityMetricMapper trackingQualityMetricMapper;

    public TrackingQualityMetricRepositoryImpl(TrackingQualityMetricMapper trackingQualityMetricMapper) {
        this.trackingQualityMetricMapper = trackingQualityMetricMapper;
    }

    @Override
    public TrackingQualityMetric findById(String id) {
        TrackingQualityMetricDO dos = trackingQualityMetricMapper.selectById(Long.parseLong(id));
        return TrackingQualityInfraConvert.INSTANCE.toDomain(dos);
    }

    @Override
    public com.cyan.arch.common.api.Page<TrackingQualityMetric> page(TrackingQualityMetricPageQuery query) {
        Page<TrackingQualityMetricDO> page = new Page<>(query.current(), query.size());
        LambdaQueryWrapper<TrackingQualityMetricDO> wrapper = new LambdaQueryWrapper<TrackingQualityMetricDO>()
                .eq(StringUtils.isNotBlank(query.getAppCode()), TrackingQualityMetricDO::getAppCode, query.getAppCode())
                .eq(StringUtils.isNotBlank(query.getEventCode()), TrackingQualityMetricDO::getEventCode, query.getEventCode())
                .orderByDesc(TrackingQualityMetricDO::getMetricTime);
        Page<TrackingQualityMetricDO> result = trackingQualityMetricMapper.selectPage(page, wrapper);
        List<TrackingQualityMetric> list = Optional.ofNullable(result.getRecords()).orElse(List.of()).stream()
                .map(TrackingQualityInfraConvert.INSTANCE::toDomain)
                .toList();
        return new com.cyan.arch.common.api.Page<>(list, result.getCurrent(), result.getSize(), result.getTotal());
    }

    @Override
    public TrackingQualityMetric save(TrackingQualityMetric metric) {
        TrackingQualityMetricDO dos = TrackingQualityInfraConvert.INSTANCE.toDO(metric);
        trackingQualityMetricMapper.insert(dos);
        return findById(String.valueOf(dos.getId()));
    }

    @Override
    public TrackingQualityMetric update(TrackingQualityMetric metric) {
        TrackingQualityMetricDO dos = TrackingQualityInfraConvert.INSTANCE.toDO(metric);
        dos.setId(Long.parseLong(metric.getId()));
        trackingQualityMetricMapper.updateById(dos);
        return findById(metric.getId());
    }

    @Override
    public void deleteById(String id) {
        trackingQualityMetricMapper.deleteById(Long.parseLong(id));
    }
}

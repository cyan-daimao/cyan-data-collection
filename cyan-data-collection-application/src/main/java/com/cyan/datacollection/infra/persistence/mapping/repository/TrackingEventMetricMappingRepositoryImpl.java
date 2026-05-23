package com.cyan.datacollection.infra.persistence.mapping.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyan.datacollection.domain.mapping.TrackingEventMetricMapping;
import com.cyan.datacollection.domain.mapping.repository.TrackingEventMetricMappingRepository;
import com.cyan.datacollection.infra.persistence.mapping.convert.TrackingMappingInfraConvert;
import com.cyan.datacollection.infra.persistence.mapping.dos.TrackingEventMetricMappingDO;
import com.cyan.datacollection.infra.persistence.mapping.mappers.TrackingEventMetricMappingMapper;
import org.springframework.stereotype.Repository;

/**
 * 采集事件指标映射仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingEventMetricMappingRepositoryImpl implements TrackingEventMetricMappingRepository {

    private final TrackingEventMetricMappingMapper mapper;

    public TrackingEventMetricMappingRepositoryImpl(TrackingEventMetricMappingMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public TrackingEventMetricMapping findByEventId(String eventId) {
        TrackingEventMetricMappingDO data = mapper.selectOne(new LambdaQueryWrapper<TrackingEventMetricMappingDO>()
                .eq(TrackingEventMetricMappingDO::getEventId, Long.parseLong(eventId)));
        return TrackingMappingInfraConvert.INSTANCE.toEventMetricMapping(data);
    }

    @Override
    public TrackingEventMetricMapping findByMetricCode(String metricCode) {
        TrackingEventMetricMappingDO data = mapper.selectOne(new LambdaQueryWrapper<TrackingEventMetricMappingDO>()
                .eq(TrackingEventMetricMappingDO::getMetricCode, metricCode));
        return TrackingMappingInfraConvert.INSTANCE.toEventMetricMapping(data);
    }

    @Override
    public TrackingEventMetricMapping save(TrackingEventMetricMapping mapping) {
        TrackingEventMetricMappingDO data = TrackingMappingInfraConvert.INSTANCE.toEventMetricMappingDO(mapping);
        mapper.insert(data);
        return TrackingMappingInfraConvert.INSTANCE.toEventMetricMapping(mapper.selectById(data.getId()));
    }

    @Override
    public TrackingEventMetricMapping update(TrackingEventMetricMapping mapping) {
        TrackingEventMetricMappingDO data = TrackingMappingInfraConvert.INSTANCE.toEventMetricMappingDO(mapping);
        mapper.updateById(data);
        return TrackingMappingInfraConvert.INSTANCE.toEventMetricMapping(mapper.selectById(data.getId()));
    }
}

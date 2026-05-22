package com.cyan.datacollection.infra.persistence.event.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyan.arch.common.api.Pageable;
import com.cyan.datacollection.domain.event.TrackingEvent;
import com.cyan.datacollection.domain.event.query.TrackingEventPageQuery;
import com.cyan.datacollection.domain.event.repository.TrackingEventRepository;
import com.cyan.datacollection.enums.EventStatus;
import com.cyan.datacollection.enums.EventType;
import com.cyan.datacollection.infra.persistence.event.convert.TrackingEventInfraConvert;
import com.cyan.datacollection.infra.persistence.event.dos.TrackingEventDO;
import com.cyan.datacollection.infra.persistence.event.mappers.TrackingEventMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 事件定义仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingEventRepositoryImpl implements TrackingEventRepository {

    private final TrackingEventMapper trackingEventMapper;

    public TrackingEventRepositoryImpl(TrackingEventMapper trackingEventMapper) {
        this.trackingEventMapper = trackingEventMapper;
    }

    @Override
    public TrackingEvent findById(String id) {
        TrackingEventDO trackingEventDO = trackingEventMapper.selectById(Long.parseLong(id));
        return TrackingEventInfraConvert.INSTANCE.toTrackingEvent(trackingEventDO);
    }

    @Override
    public com.cyan.arch.common.api.Page<TrackingEvent> page(TrackingEventPageQuery query) {
        Page<TrackingEventDO> page = new Page<>(query.current(), query.size());
        LambdaQueryWrapper<TrackingEventDO> wrapper = new LambdaQueryWrapper<TrackingEventDO>()
                .like(StringUtils.isNotBlank(query.getEventCode()), TrackingEventDO::getEventCode, query.getEventCode())
                .like(StringUtils.isNotBlank(query.getEventName()), TrackingEventDO::getEventName, query.getEventName())
                .eq(StringUtils.isNotBlank(query.getEventType()), TrackingEventDO::getEventType, EventType.of(query.getEventType()))
                .eq(StringUtils.isNotBlank(query.getBusinessDomain()), TrackingEventDO::getBusinessDomain, query.getBusinessDomain())
                .eq(StringUtils.isNotBlank(query.getStatus()), TrackingEventDO::getStatus, EventStatus.of(query.getStatus()))
                .eq(query.getIsCore() != null, TrackingEventDO::getIsCore, query.getIsCore())
                .orderByDesc(TrackingEventDO::getUpdatedAt);
        Page<TrackingEventDO> result = trackingEventMapper.selectPage(page, wrapper);
        List<TrackingEvent> list = Optional.ofNullable(result.getRecords()).orElse(List.of()).stream()
                .map(TrackingEventInfraConvert.INSTANCE::toTrackingEvent)
                .toList();
        return new com.cyan.arch.common.api.Page<>(list, result.getCurrent(), result.getSize(), result.getTotal());
    }

    @Override
    public TrackingEvent findByCode(String eventCode) {
        LambdaQueryWrapper<TrackingEventDO> wrapper = new LambdaQueryWrapper<TrackingEventDO>()
                .eq(TrackingEventDO::getEventCode, eventCode);
        TrackingEventDO trackingEventDO = trackingEventMapper.selectOne(wrapper);
        return TrackingEventInfraConvert.INSTANCE.toTrackingEvent(trackingEventDO);
    }

    @Override
    public TrackingEvent save(TrackingEvent event) {
        TrackingEventDO trackingEventDO = TrackingEventInfraConvert.INSTANCE.toTrackingEventDO(event);
        trackingEventMapper.insert(trackingEventDO);
        return findById(String.valueOf(trackingEventDO.getId()));
    }

    @Override
    public TrackingEvent update(TrackingEvent event) {
        TrackingEventDO trackingEventDO = TrackingEventInfraConvert.INSTANCE.toTrackingEventDO(event);
        trackingEventDO.setId(Long.parseLong(event.getId()));
        trackingEventMapper.updateById(trackingEventDO);
        return findById(event.getId());
    }

    @Override
    public void deleteById(String id) {
        trackingEventMapper.deleteById(Long.parseLong(id));
    }

    @Override
    public List<TrackingEvent> findByIds(List<String> ids) {
        List<Long> idList = ids.stream().map(Long::parseLong).toList();
        List<TrackingEventDO> dos = trackingEventMapper.selectBatchIds(idList);
        return Optional.ofNullable(dos).orElse(List.of()).stream()
                .map(TrackingEventInfraConvert.INSTANCE::toTrackingEvent)
                .toList();
    }
}

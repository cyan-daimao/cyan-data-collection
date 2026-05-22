package com.cyan.datacollection.infra.persistence.eventproperty.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyan.datacollection.domain.eventproperty.TrackingEventProperty;
import com.cyan.datacollection.domain.eventproperty.repository.TrackingEventPropertyRepository;
import com.cyan.datacollection.infra.persistence.eventproperty.convert.TrackingEventPropertyInfraConvert;
import com.cyan.datacollection.infra.persistence.eventproperty.dos.TrackingEventPropertyDO;
import com.cyan.datacollection.infra.persistence.eventproperty.mappers.TrackingEventPropertyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 事件属性关系仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingEventPropertyRepositoryImpl implements TrackingEventPropertyRepository {

    private final TrackingEventPropertyMapper trackingEventPropertyMapper;

    public TrackingEventPropertyRepositoryImpl(TrackingEventPropertyMapper trackingEventPropertyMapper) {
        this.trackingEventPropertyMapper = trackingEventPropertyMapper;
    }

    @Override
    public TrackingEventProperty findById(String id) {
        TrackingEventPropertyDO trackingEventPropertyDO = trackingEventPropertyMapper.selectById(Long.parseLong(id));
        return TrackingEventPropertyInfraConvert.INSTANCE.toTrackingEventProperty(trackingEventPropertyDO);
    }

    @Override
    public List<TrackingEventProperty> findByEventId(String eventId) {
        LambdaQueryWrapper<TrackingEventPropertyDO> wrapper = new LambdaQueryWrapper<TrackingEventPropertyDO>()
                .eq(TrackingEventPropertyDO::getEventId, Long.parseLong(eventId));
        List<TrackingEventPropertyDO> dos = trackingEventPropertyMapper.selectList(wrapper);
        return Optional.ofNullable(dos).orElse(List.of()).stream()
                .map(TrackingEventPropertyInfraConvert.INSTANCE::toTrackingEventProperty)
                .toList();
    }

    @Override
    public List<TrackingEventProperty> findByPropertyId(String propertyId) {
        LambdaQueryWrapper<TrackingEventPropertyDO> wrapper = new LambdaQueryWrapper<TrackingEventPropertyDO>()
                .eq(TrackingEventPropertyDO::getPropertyId, Long.parseLong(propertyId));
        List<TrackingEventPropertyDO> dos = trackingEventPropertyMapper.selectList(wrapper);
        return Optional.ofNullable(dos).orElse(List.of()).stream()
                .map(TrackingEventPropertyInfraConvert.INSTANCE::toTrackingEventProperty)
                .toList();
    }

    @Override
    public TrackingEventProperty save(TrackingEventProperty eventProperty) {
        TrackingEventPropertyDO trackingEventPropertyDO = TrackingEventPropertyInfraConvert.INSTANCE.toTrackingEventPropertyDO(eventProperty);
        trackingEventPropertyMapper.insert(trackingEventPropertyDO);
        return findById(String.valueOf(trackingEventPropertyDO.getId()));
    }

    @Override
    public TrackingEventProperty update(TrackingEventProperty eventProperty) {
        TrackingEventPropertyDO trackingEventPropertyDO = TrackingEventPropertyInfraConvert.INSTANCE.toTrackingEventPropertyDO(eventProperty);
        trackingEventPropertyDO.setId(Long.parseLong(eventProperty.getId()));
        trackingEventPropertyMapper.updateById(trackingEventPropertyDO);
        return findById(eventProperty.getId());
    }

    @Override
    public void deleteById(String id) {
        trackingEventPropertyMapper.deleteById(Long.parseLong(id));
    }

    @Override
    public void deleteByEventId(String eventId) {
        LambdaQueryWrapper<TrackingEventPropertyDO> wrapper = new LambdaQueryWrapper<TrackingEventPropertyDO>()
                .eq(TrackingEventPropertyDO::getEventId, Long.parseLong(eventId));
        trackingEventPropertyMapper.delete(wrapper);
    }

    @Override
    public void deleteByPropertyId(String propertyId) {
        LambdaQueryWrapper<TrackingEventPropertyDO> wrapper = new LambdaQueryWrapper<TrackingEventPropertyDO>()
                .eq(TrackingEventPropertyDO::getPropertyId, Long.parseLong(propertyId));
        trackingEventPropertyMapper.delete(wrapper);
    }
}

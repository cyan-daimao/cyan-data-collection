package com.cyan.datacollection.infra.persistence.eventproperty.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.alibaba.fastjson2.JSON;
import com.cyan.datacollection.enums.DataType;
import com.cyan.datacollection.domain.eventproperty.EventPropertyRule;
import com.cyan.datacollection.domain.eventproperty.TrackingEventProperty;
import com.cyan.datacollection.domain.eventproperty.repository.TrackingEventPropertyRepository;
import com.cyan.datacollection.infra.persistence.eventproperty.convert.TrackingEventPropertyInfraConvert;
import com.cyan.datacollection.infra.persistence.eventproperty.dos.TrackingEventPropertyDO;
import com.cyan.datacollection.infra.persistence.eventproperty.mappers.TrackingEventPropertyMapper;
import com.cyan.datacollection.infra.persistence.property.dos.TrackingPropertyDO;
import com.cyan.datacollection.infra.persistence.property.mappers.TrackingPropertyMapper;
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
    private final TrackingPropertyMapper trackingPropertyMapper;

    public TrackingEventPropertyRepositoryImpl(TrackingEventPropertyMapper trackingEventPropertyMapper,
                                               TrackingPropertyMapper trackingPropertyMapper) {
        this.trackingEventPropertyMapper = trackingEventPropertyMapper;
        this.trackingPropertyMapper = trackingPropertyMapper;
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

    @Override
    public List<EventPropertyRule> findPropertyRulesByEventId(String eventId) {
        // 查询事件属性关系
        LambdaQueryWrapper<TrackingEventPropertyDO> wrapper = new LambdaQueryWrapper<TrackingEventPropertyDO>()
                .eq(TrackingEventPropertyDO::getEventId, Long.parseLong(eventId));
        List<TrackingEventPropertyDO> eventPropertyDOs = trackingEventPropertyMapper.selectList(wrapper);

        if (eventPropertyDOs == null || eventPropertyDOs.isEmpty()) {
            return List.of();
        }

        // 查询关联的属性定义
        List<Long> propertyIds = eventPropertyDOs.stream()
                .map(TrackingEventPropertyDO::getPropertyId)
                .toList();
        List<TrackingPropertyDO> propertyDOs = trackingPropertyMapper.selectBatchIds(propertyIds);

        // 构建属性ID -> 属性定义的映射
        var propertyMap = propertyDOs.stream()
                .collect(java.util.stream.Collectors.toMap(TrackingPropertyDO::getId, p -> p));

        // 组装规则列表
        return eventPropertyDOs.stream()
                .map(ep -> {
                    TrackingPropertyDO prop = propertyMap.get(ep.getPropertyId());
                    if (prop == null) {
                        return null;
                    }
                    return new EventPropertyRule()
                            .setPropertyCode(prop.getPropertyCode())
                            .setPropertyName(prop.getPropertyName())
                            .setDataType(prop.getDataType())
                            .setIsRequired(ep.getIsRequired())
                            .setIsSensitive(prop.getIsSensitive())
                            .setEnumValues(prop.getEnumValues() != null ? JSON.parseArray(prop.getEnumValues(), String.class) : null)
                            .setMaxLength(prop.getMaxLength())
                            .setValidationRule(prop.getValidationRule())
                            .setDefaultValue(ep.getDefaultValue());
                })
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toList());
    }
}

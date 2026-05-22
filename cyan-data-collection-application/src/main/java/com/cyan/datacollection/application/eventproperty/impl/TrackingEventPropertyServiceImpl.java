package com.cyan.datacollection.application.eventproperty.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.eventproperty.TrackingEventPropertyService;
import com.cyan.datacollection.application.eventproperty.bo.EventPropertyBO;
import com.cyan.datacollection.application.eventproperty.cmd.EventPropertyConfigCmd;
import com.cyan.datacollection.domain.event.TrackingEvent;
import com.cyan.datacollection.domain.event.repository.TrackingEventRepository;
import com.cyan.datacollection.domain.eventproperty.TrackingEventProperty;
import com.cyan.datacollection.domain.eventproperty.repository.TrackingEventPropertyRepository;
import com.cyan.datacollection.domain.property.TrackingProperty;
import com.cyan.datacollection.domain.property.repository.TrackingPropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件属性关系服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingEventPropertyServiceImpl implements TrackingEventPropertyService {

    private final TrackingEventRepository trackingEventRepository;
    private final TrackingEventPropertyRepository trackingEventPropertyRepository;
    private final TrackingPropertyRepository trackingPropertyRepository;

    public TrackingEventPropertyServiceImpl(TrackingEventRepository trackingEventRepository,
                                            TrackingEventPropertyRepository trackingEventPropertyRepository,
                                            TrackingPropertyRepository trackingPropertyRepository) {
        this.trackingEventRepository = trackingEventRepository;
        this.trackingEventPropertyRepository = trackingEventPropertyRepository;
        this.trackingPropertyRepository = trackingPropertyRepository;
    }

    @Override
    @Transactional
    public void configProperties(String eventId, List<EventPropertyConfigCmd> cmds) {
        TrackingEvent event = trackingEventRepository.findById(eventId);
        Assert.notNull(event, new SilentException("事件不存在"));

        Assert.notEmpty(cmds, new SilentException("属性配置列表不能为空"));

        // 校验所有属性ID存在
        List<String> propertyIds = cmds.stream().map(EventPropertyConfigCmd::getPropertyId).toList();
        List<TrackingProperty> properties = trackingPropertyRepository.findByIds(propertyIds);
        if (properties.size() != propertyIds.size()) {
            throw new SilentException("存在无效的属性ID");
        }

        // 删除旧关系，全量替换
        trackingEventPropertyRepository.deleteByEventId(eventId);

        // 批量新建
        for (EventPropertyConfigCmd cmd : cmds) {
            TrackingEventProperty ep = new TrackingEventProperty();
            ep.setEventId(eventId);
            ep.setPropertyId(cmd.getPropertyId());
            ep.setIsRequired(cmd.getIsRequired());
            ep.setDefaultValue(cmd.getDefaultValue());
            ep.setSampleValue(cmd.getSampleValue());
            ep.setDescription(cmd.getDescription());
            ep.save(trackingEventPropertyRepository);
        }
    }

    @Override
    public List<EventPropertyBO> listProperties(String eventId) {
        TrackingEvent event = trackingEventRepository.findById(eventId);
        Assert.notNull(event, new SilentException("事件不存在"));

        List<TrackingEventProperty> relations = trackingEventPropertyRepository.findByEventId(eventId);
        List<EventPropertyBO> result = new ArrayList<>();
        for (TrackingEventProperty relation : relations) {
            TrackingProperty property = trackingPropertyRepository.findById(relation.getPropertyId());
            if (property == null) {
                continue;
            }
            EventPropertyBO bo = new EventPropertyBO()
                    .setId(relation.getId())
                    .setEventId(relation.getEventId())
                    .setPropertyId(relation.getPropertyId())
                    .setPropertyCode(property.getPropertyCode())
                    .setPropertyName(property.getPropertyName())
                    .setDataType(property.getDataType())
                    .setEnumValues(property.getEnumValues())
                    .setMaxLength(property.getMaxLength())
                    .setValidationRule(property.getValidationRule())
                    .setIsSensitive(property.getIsSensitive())
                    .setIsRequired(relation.getIsRequired())
                    .setDefaultValue(relation.getDefaultValue())
                    .setSampleValue(relation.getSampleValue())
                    .setDescription(relation.getDescription());
            result.add(bo);
        }
        return result;
    }
}

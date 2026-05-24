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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件属性关系服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingEventPropertyServiceImpl implements TrackingEventPropertyService {

    /**
     * 系统协议属性默认必填配置
     */
    private static final Map<String, Boolean> SYSTEM_PROPERTY_REQUIRED_MAP = Map.ofEntries(
            Map.entry("app_code", true),
            Map.entry("terminal_type", true),
            Map.entry("environment", true),
            Map.entry("anonymous_id", true),
            Map.entry("session_id", true),
            Map.entry("page_code", true),
            Map.entry("event_code", true),
            Map.entry("event_time", true),
            Map.entry("event_type", true),
            Map.entry("request_id", true),
            Map.entry("debug_token", false)
    );

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

        List<EventPropertyConfigCmd> mergedCmds = mergeSystemProperties(cmds);

        // 校验所有属性ID存在
        List<String> propertyIds = mergedCmds.stream().map(EventPropertyConfigCmd::getPropertyId).toList();
        List<TrackingProperty> properties = trackingPropertyRepository.findByIds(propertyIds);
        if (properties.size() != propertyIds.size()) {
            throw new SilentException("存在无效的属性ID");
        }
        properties.forEach(TrackingProperty::validateBindableToEvent);

        // 删除旧关系，全量替换
        trackingEventPropertyRepository.deleteByEventId(eventId);

        // 批量新建
        for (EventPropertyConfigCmd cmd : mergedCmds) {
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
    @Transactional
    public void bindSystemProperties(String eventId) {
        TrackingEvent event = trackingEventRepository.findById(eventId);
        Assert.notNull(event, new SilentException("事件不存在"));

        List<TrackingEventProperty> existingRelations = trackingEventPropertyRepository.findByEventId(eventId);
        List<String> existingPropertyIds = existingRelations.stream()
                .map(TrackingEventProperty::getPropertyId)
                .toList();

        for (EventPropertyConfigCmd cmd : buildSystemPropertyCmds()) {
            if (existingPropertyIds.contains(cmd.getPropertyId())) {
                continue;
            }
            TrackingEventProperty ep = new TrackingEventProperty();
            ep.setEventId(eventId);
            ep.setPropertyId(cmd.getPropertyId());
            ep.setIsRequired(cmd.getIsRequired());
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

    /**
     * 合并用户配置与系统属性配置
     */
    private List<EventPropertyConfigCmd> mergeSystemProperties(List<EventPropertyConfigCmd> cmds) {
        Map<String, EventPropertyConfigCmd> merged = new LinkedHashMap<>();
        if (cmds != null) {
            for (EventPropertyConfigCmd cmd : cmds) {
                merged.put(cmd.getPropertyId(), cmd);
            }
        }
        for (EventPropertyConfigCmd systemCmd : buildSystemPropertyCmds()) {
            merged.put(systemCmd.getPropertyId(), systemCmd);
        }
        return new ArrayList<>(merged.values());
    }

    /**
     * 构造系统属性配置命令
     */
    private List<EventPropertyConfigCmd> buildSystemPropertyCmds() {
        List<EventPropertyConfigCmd> result = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : SYSTEM_PROPERTY_REQUIRED_MAP.entrySet()) {
            TrackingProperty property = trackingPropertyRepository.findByCode(entry.getKey());
            if (property == null) {
                log.warn("[TrackingEventProperty] 系统属性不存在，跳过自动绑定: {}", entry.getKey());
                continue;
            }
            property.validateBindableToEvent();
            result.add(new EventPropertyConfigCmd()
                    .setPropertyId(property.getId())
                    .setIsRequired(entry.getValue())
                    .setDescription("系统协议字段: " + entry.getKey()));
        }
        return result;
    }
}

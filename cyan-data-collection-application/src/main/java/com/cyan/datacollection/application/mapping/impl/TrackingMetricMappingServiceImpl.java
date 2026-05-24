package com.cyan.datacollection.application.mapping.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Response;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.mapping.TrackingMetricMappingService;
import com.cyan.datacollection.application.mapping.bo.EventMetricMappingBO;
import com.cyan.datacollection.application.mapping.bo.PropertyDimensionMappingBO;
import com.cyan.datacollection.application.mapping.cmd.EventMetricSyncCmd;
import com.cyan.datacollection.application.mapping.cmd.PropertyDimensionSyncCmd;
import com.cyan.datacollection.application.mapping.convert.TrackingMappingAppConvert;
import com.cyan.datacollection.domain.event.TrackingEvent;
import com.cyan.datacollection.domain.event.repository.TrackingEventRepository;
import com.cyan.datacollection.domain.mapping.TrackingEventMetricMapping;
import com.cyan.datacollection.domain.mapping.TrackingPropertyDimensionMapping;
import com.cyan.datacollection.domain.mapping.repository.TrackingEventMetricMappingRepository;
import com.cyan.datacollection.domain.mapping.repository.TrackingPropertyDimensionMappingRepository;
import com.cyan.datacollection.domain.property.TrackingProperty;
import com.cyan.datacollection.domain.property.repository.TrackingPropertyRepository;
import com.cyan.datacollection.enums.DataType;
import com.cyan.datacollection.enums.EventType;
import com.cyan.datacollection.enums.SyncStatus;
import com.cyan.datametric.client.collection.MetricCollectionMappingClient;
import com.cyan.datametric.client.collection.dto.CollectionDimensionMappingDTO;
import com.cyan.datametric.client.collection.dto.CollectionMetricMappingDTO;
import com.cyan.datametric.client.collection.request.CollectionDimensionUpsertRequest;
import com.cyan.datametric.client.collection.request.CollectionMetricUpsertRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 采集指标平台映射服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingMetricMappingServiceImpl implements TrackingMetricMappingService {

    private final TrackingPropertyRepository propertyRepository;
    private final TrackingEventRepository eventRepository;
    private final TrackingPropertyDimensionMappingRepository propertyDimensionMappingRepository;
    private final TrackingEventMetricMappingRepository eventMetricMappingRepository;
    private final MetricCollectionMappingClient metricCollectionMappingClient;

    public TrackingMetricMappingServiceImpl(TrackingPropertyRepository propertyRepository,
                                            TrackingEventRepository eventRepository,
                                            TrackingPropertyDimensionMappingRepository propertyDimensionMappingRepository,
                                            TrackingEventMetricMappingRepository eventMetricMappingRepository,
                                            MetricCollectionMappingClient metricCollectionMappingClient) {
        this.propertyRepository = propertyRepository;
        this.eventRepository = eventRepository;
        this.propertyDimensionMappingRepository = propertyDimensionMappingRepository;
        this.eventMetricMappingRepository = eventMetricMappingRepository;
        this.metricCollectionMappingClient = metricCollectionMappingClient;
    }

    @Override
    @Transactional
    public PropertyDimensionMappingBO syncPropertyDimension(String propertyId, PropertyDimensionSyncCmd cmd) {
        TrackingProperty property = propertyRepository.findById(propertyId);
        Assert.notNull(property, new SilentException("属性不存在"));
        String operator = defaultString(cmd.getOperator(), "system");
        String dimCode = defaultString(cmd.getDimCode(), "dim_" + property.getPropertyCode());
        TrackingPropertyDimensionMapping mapping = propertyDimensionMappingRepository.findByPropertyId(propertyId);
        if (mapping == null) {
            mapping = new TrackingPropertyDimensionMapping()
                    .setPropertyId(property.getId())
                    .setPropertyCode(property.getPropertyCode())
                    .setDimCode(dimCode)
                    .setSyncStatus(SyncStatus.PENDING)
                    .setCreateBy(operator)
                    .setUpdateBy(operator)
                    .save(propertyDimensionMappingRepository);
        } else {
            mapping.setDimCode(dimCode);
            mapping.setSyncStatus(SyncStatus.PENDING);
            mapping.setErrorMessage(null);
            mapping.setUpdateBy(operator);
            mapping = mapping.update(propertyDimensionMappingRepository);
        }

        try {
            Response<CollectionDimensionMappingDTO> response = metricCollectionMappingClient.upsertDimensionFromProperty(buildDimensionRequest(property, cmd, dimCode, operator));
            Assert.notNull(response, new SilentException("同步维度失败: 返回为空"));
            Assert.notNull(response.getData(), new SilentException("同步维度失败: " + response.getMessage()));
            mapping.markSuccess(response.getData().getDimId(), response.getData().getDimCode(), operator);
        } catch (Exception e) {
            log.error("属性同步维度失败, propertyId={}", propertyId, e);
            mapping.markFailed(e.getMessage(), operator);
        }
        mapping = mapping.update(propertyDimensionMappingRepository);
        return TrackingMappingAppConvert.INSTANCE.toPropertyDimensionMappingBO(mapping);
    }

    @Override
    public PropertyDimensionMappingBO getPropertyDimensionMapping(String propertyId) {
        return TrackingMappingAppConvert.INSTANCE.toPropertyDimensionMappingBO(propertyDimensionMappingRepository.findByPropertyId(propertyId));
    }

    @Override
    @Transactional
    public EventMetricMappingBO syncEventMetric(String eventId, EventMetricSyncCmd cmd) {
        TrackingEvent event = eventRepository.findById(eventId);
        Assert.notNull(event, new SilentException("事件不存在"));
        String operator = defaultString(cmd.getOperator(), "system");
        String metricCode = defaultString(cmd.getMetricCode(), event.getEventCode() + "_count");
        TrackingEventMetricMapping mapping = eventMetricMappingRepository.findByEventId(eventId);
        if (mapping == null) {
            mapping = new TrackingEventMetricMapping()
                    .setEventId(event.getId())
                    .setEventCode(event.getEventCode())
                    .setMetricCode(metricCode)
                    .setSyncStatus(SyncStatus.PENDING)
                    .setCreateBy(operator)
                    .setUpdateBy(operator)
                    .save(eventMetricMappingRepository);
        } else {
            mapping.setMetricCode(metricCode);
            mapping.setSyncStatus(SyncStatus.PENDING);
            mapping.setErrorMessage(null);
            mapping.setUpdateBy(operator);
            mapping = mapping.update(eventMetricMappingRepository);
        }

        try {
            Response<CollectionMetricMappingDTO> response = metricCollectionMappingClient.upsertAtomicMetricFromEvent(buildMetricRequest(event, cmd, metricCode, operator));
            Assert.notNull(response, new SilentException("同步指标失败: 返回为空"));
            Assert.notNull(response.getData(), new SilentException("同步指标失败: " + response.getMessage()));
            mapping.markSuccess(response.getData().getMetricId(), response.getData().getMetricCode(), operator);
        } catch (Exception e) {
            log.error("事件同步指标失败, eventId={}", eventId, e);
            mapping.markFailed(e.getMessage(), operator);
        }
        mapping = mapping.update(eventMetricMappingRepository);
        return TrackingMappingAppConvert.INSTANCE.toEventMetricMappingBO(mapping);
    }

    @Override
    public EventMetricMappingBO getEventMetricMapping(String eventId) {
        return TrackingMappingAppConvert.INSTANCE.toEventMetricMappingBO(eventMetricMappingRepository.findByEventId(eventId));
    }

    private CollectionDimensionUpsertRequest buildDimensionRequest(TrackingProperty property,
                                                                   PropertyDimensionSyncCmd cmd,
                                                                   String dimCode,
                                                                   String operator) {
        return new CollectionDimensionUpsertRequest()
                .setPropertyId(property.getId())
                .setPropertyCode(property.getPropertyCode())
                .setPropertyName(property.getPropertyName())
                .setDimCode(dimCode)
                .setDimName(defaultString(cmd.getDimName(), property.getPropertyName()))
                .setDimType(defaultString(cmd.getDimType(), "STRING"))
                .setDataType(resolveMetricDataType(property))
                .setDimValues(property.getEnumValues())
                .setCategoryId(cmd.getCategoryId())
                .setSourceTable("ods_tracking_frontend_other_event")
                .setSourceType("JSON_PATH")
                .setSourceExpr(resolvePropertySourceExpr(property.getPropertyCode()))
                .setColumnName(property.getPropertyCode())
                .setDescription(defaultString(property.getDescription(), "采集属性 " + property.getPropertyCode()))
                .setOwner(defaultString(cmd.getOwner(), "system"))
                .setOperator(operator);
    }

    private CollectionMetricUpsertRequest buildMetricRequest(TrackingEvent event,
                                                             EventMetricSyncCmd cmd,
                                                             String metricCode,
                                                             String operator) {
        return new CollectionMetricUpsertRequest()
                .setEventId(event.getId())
                .setEventCode(event.getEventCode())
                .setEventName(event.getEventName())
                .setMetricCode(metricCode)
                .setMetricName(defaultString(cmd.getMetricName(), event.getEventName() + "次数"))
                .setSubjectCode(defaultString(cmd.getSubjectCode(), "data_collection"))
                .setStatFunc(defaultString(cmd.getStatFunc(), "COUNT"))
                .setDsName("iceberg")
                .setDbName("ods")
                .setTblName(resolveOdsTableName(event))
                .setColName("request_id")
                .setFilterCondition(List.of(new CollectionMetricUpsertRequest.FilterConditionRequest()
                        .setField("event_code")
                        .setOp("=")
                        .setValue(event.getEventCode())))
                .setBizCaliber("统计事件 " + event.getEventCode() + " 的触发次数")
                .setTechCaliber("从 ods." + resolveOdsTableName(event) + " 按 event_code='" + event.getEventCode() + "' 过滤后 COUNT(request_id)")
                .setSecurityLevel(defaultString(cmd.getSecurityLevel(), "L1"))
                .setOwner(defaultString(cmd.getOwner(), event.getOwner()))
                .setOperator(operator);
    }

    private String resolveOdsTableName(TrackingEvent event) {
        EventType eventType = event.getEventType();
        if (eventType == EventType.CLICK) {
            return "ods_tracking_frontend_click_event";
        }
        if (eventType == EventType.EXPOSURE) {
            return "ods_tracking_frontend_exposure_event";
        }
        if (eventType == EventType.PAGE_ENTER || eventType == EventType.PAGE_VIEW) {
            return "ods_tracking_frontend_page_enter_event";
        }
        if (eventType == EventType.PAGE_EXIT) {
            return "ods_tracking_frontend_page_exit_event";
        }
        if (eventType == EventType.SYSTEM || eventType == EventType.BUSINESS) {
            return "ods_tracking_backend_" + event.getEventCode();
        }
        return "ods_tracking_frontend_other_event";
    }

    private String resolveMetricDataType(TrackingProperty property) {
        if (property.getDataType() == null) {
            return "STRING";
        }
        DataType dataType = property.getDataType();
        if (dataType == DataType.NUMBER) {
            return "DECIMAL";
        }
        if (dataType == DataType.BOOLEAN) {
            return "BOOLEAN";
        }
        if (dataType == DataType.DATE) {
            return "DATE";
        }
        if (dataType == DataType.DATETIME) {
            return "DATETIME";
        }
        return "STRING";
    }

    private String resolvePropertySourceExpr(String propertyCode) {
        return "COALESCE("
                + "$.business." + propertyCode + ","
                + "$.action." + propertyCode + ","
                + "$.common." + propertyCode + ","
                + "$.extra." + propertyCode
                + ")";
    }

    private String defaultString(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}

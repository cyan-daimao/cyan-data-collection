package com.cyan.datacollection.application.collect.impl;

import com.alibaba.fastjson2.JSON;
import com.cyan.datacollection.application.collect.TrackingCollectService;
import com.cyan.datacollection.application.collect.TrackingEventValidateService;
import com.cyan.datacollection.application.collect.bo.CollectResultBO;
import com.cyan.datacollection.application.collect.bo.ValidateResultBO;
import com.cyan.datacollection.application.collect.cmd.EventCollectCmd;
import com.cyan.datacollection.domain.app.TrackingApp;
import com.cyan.datacollection.domain.app.repository.TrackingAppRepository;
import com.cyan.datacollection.domain.collect.TrackingEventSample;
import com.cyan.datacollection.domain.collect.repository.TrackingEventSampleRepository;
import com.cyan.datacollection.domain.debug.TrackingDebugSession;
import com.cyan.datacollection.domain.debug.repository.TrackingDebugSessionRepository;
import com.cyan.datacollection.domain.event.TrackingEvent;
import com.cyan.datacollection.domain.event.repository.TrackingEventRepository;
import com.cyan.datacollection.enums.AppStatus;
import com.cyan.datacollection.enums.EventStatus;
import com.cyan.datacollection.enums.TerminalType;
import com.cyan.datacollection.enums.ValidateStatus;
import com.cyan.datacollection.infra.kafka.TrackingEventKafkaProducer;
import com.cyan.datacollection.infra.metrics.CollectMetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件上报服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingCollectServiceImpl implements TrackingCollectService {

    private final TrackingAppRepository trackingAppRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final TrackingEventSampleRepository trackingEventSampleRepository;
    private final TrackingDebugSessionRepository trackingDebugSessionRepository;
    private final TrackingEventValidateService trackingEventValidateService;
    private final TrackingEventKafkaProducer trackingEventKafkaProducer;
    private final CollectMetricsService collectMetricsService;

    @Value("${data-collection.kafka.send-sync:false}")
    private boolean sendSync;

    public TrackingCollectServiceImpl(TrackingAppRepository trackingAppRepository,
                                      TrackingEventRepository trackingEventRepository,
                                      TrackingEventSampleRepository trackingEventSampleRepository,
                                      TrackingDebugSessionRepository trackingDebugSessionRepository,
                                      TrackingEventValidateService trackingEventValidateService,
                                      TrackingEventKafkaProducer trackingEventKafkaProducer,
                                      CollectMetricsService collectMetricsService) {
        this.trackingAppRepository = trackingAppRepository;
        this.trackingEventRepository = trackingEventRepository;
        this.trackingEventSampleRepository = trackingEventSampleRepository;
        this.trackingDebugSessionRepository = trackingDebugSessionRepository;
        this.trackingEventValidateService = trackingEventValidateService;
        this.trackingEventKafkaProducer = trackingEventKafkaProducer;
        this.collectMetricsService = collectMetricsService;
    }

    @Override
    @Transactional
    public CollectResultBO collect(EventCollectCmd cmd) {
        long startTime = System.currentTimeMillis();
        List<String> errors = new ArrayList<>();
        ValidateStatus baseStatus = ValidateStatus.PASS;
        String appCode = getString(cmd.getCommon(), "appCode");
        String eventCode = getString(cmd.getAction(), "eventCode");
        String eventTimeText = getString(cmd.getAction(), "eventTime");
        String requestId = getString(cmd.getExtra(), "requestId");
        String debugToken = getString(cmd.getExtra(), "debugToken");
        String terminalType = getString(cmd.getCommon(), "terminalType");
        String environment = getString(cmd.getCommon(), "environment");
        LocalDateTime eventTime = parseEventTime(eventTimeText, errors);

        // 校验 appCode
        TrackingApp app = appCode != null ? trackingAppRepository.findByCode(appCode) : null;
        if (appCode == null || appCode.isBlank()) {
            errors.add("[FAIL] common.appCode 不能为空");
            baseStatus = ValidateStatus.FAIL;
        }
        if (app == null) {
            errors.add("[FAIL] 应用不存在: " + appCode);
            baseStatus = ValidateStatus.FAIL;
        } else if (app.getStatus() != AppStatus.ENABLED) {
            errors.add("[FAIL] 应用已禁用: " + appCode);
            baseStatus = ValidateStatus.FAIL;
        }

        // 校验 eventCode
        TrackingEvent event = appCode != null && eventCode != null ? trackingEventRepository.findByAppCodeAndCode(appCode, eventCode) : null;
        if (eventCode == null || eventCode.isBlank()) {
            errors.add("[FAIL] action.eventCode 不能为空");
            baseStatus = ValidateStatus.FAIL;
        }
        if (event == null) {
            errors.add("[FAIL] 事件不存在: " + appCode + " / " + eventCode);
            baseStatus = ValidateStatus.FAIL;
        } else if (event.getStatus() != EventStatus.PUBLISHED) {
            errors.add("[FAIL] 事件未发布: " + eventCode);
            baseStatus = ValidateStatus.FAIL;
        }

        // 校验 eventTime
        if (eventTimeText == null || eventTimeText.isBlank()) {
            errors.add("[FAIL] action.eventTime 不能为空");
            baseStatus = ValidateStatus.FAIL;
        } else if (eventTime == null) {
            baseStatus = ValidateStatus.FAIL;
        }

        // 校验 terminalType
        if (terminalType != null && TerminalType.of(terminalType) == null) {
            errors.add("[FAIL] common.terminalType 不合法: " + terminalType);
            baseStatus = ValidateStatus.FAIL;
        }

        // 如果有 debugToken，关联 Debug 会话
        if (debugToken != null && !debugToken.isEmpty()) {
            TrackingDebugSession session = trackingDebugSessionRepository.findByToken(debugToken);
            if (session == null) {
                errors.add("[FAIL] Debug Token 无效: " + debugToken);
                baseStatus = ValidateStatus.FAIL;
            }
        }

        // 属性规则校验（仅事件存在时执行）
        ValidateStatus propertyStatus = ValidateStatus.PASS;
        List<String> propertyErrors = new ArrayList<>();
        if (event != null) {
            ValidateResultBO result = trackingEventValidateService.validate(event, mergeProperties(cmd));
            propertyStatus = result.getStatus();
            propertyErrors = result.getErrors();
        }

        // 合并错误
        errors.addAll(propertyErrors);

        // 确定最终状态：基础校验或属性校验任一 FAIL 则为 FAIL；无 FAIL 但有 WARN 则为 WARN
        ValidateStatus finalStatus;
        if (baseStatus == ValidateStatus.FAIL || propertyStatus == ValidateStatus.FAIL) {
            finalStatus = ValidateStatus.FAIL;
        } else if (propertyStatus == ValidateStatus.WARN) {
            finalStatus = ValidateStatus.WARN;
        } else {
            finalStatus = ValidateStatus.PASS;
        }

        // 构造 payload
        String payload = JSON.toJSONString(cmd);
        LocalDateTime ingestionTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));

        // 写入样本
        TrackingEventSample sample = new TrackingEventSample()
                .setAppCode(appCode)
                .setEventCode(eventCode)
                .setEventTime(eventTime)
                .setIngestionTime(ingestionTime)
                .setDebugToken(debugToken)
                .setCommon(JSON.toJSONString(emptyIfNull(cmd.getCommon())))
                .setAction(JSON.toJSONString(emptyIfNull(cmd.getAction())))
                .setBusiness(JSON.toJSONString(emptyIfNull(cmd.getBusiness())))
                .setExtra(JSON.toJSONString(emptyIfNull(cmd.getExtra())))
                .setRequestId(requestId)
                .setPayload(payload)
                .setValidateStatus(finalStatus)
                .setValidateErrors(errors);

        sample = trackingEventSampleRepository.save(sample);

        // 发送 Kafka
        sendToKafka(cmd, finalStatus, errors);

        // Metrics
        collectMetricsService.recordHttpReceived(appCode, eventCode, finalStatus.name());
        collectMetricsService.recordDebugSampleSaved(appCode, eventCode, finalStatus.name());
        collectMetricsService.recordCollectDuration(appCode, eventCode, System.currentTimeMillis() - startTime);

        return new CollectResultBO()
                .setAccepted(true)
                .setSampleId(sample.getId())
                .setRequestId(requestId)
                .setValidateStatus(finalStatus)
                .setErrors(errors);
    }

    /**
     * 将事件发送到 Kafka
     */
    private void sendToKafka(EventCollectCmd cmd, ValidateStatus finalStatus, List<String> errors) {
        try {
            String appCode = getString(cmd.getCommon(), "appCode");
            String eventCode = getString(cmd.getAction(), "eventCode");
            String eventTime = getString(cmd.getAction(), "eventTime");
            String requestId = getString(cmd.getExtra(), "requestId");
            String debugToken = getString(cmd.getExtra(), "debugToken");
            Map<String, Object> message = new HashMap<>();
            message.put("requestId", requestId);
            message.put("appCode", appCode);
            message.put("eventCode", eventCode);
            message.put("eventTime", eventTime);
            message.put("ingestionTime", LocalDateTime.now(ZoneId.of("Asia/Shanghai")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            message.put("debugToken", debugToken);
            message.put("common", JSON.toJSONString(emptyIfNull(cmd.getCommon())));
            message.put("action", JSON.toJSONString(emptyIfNull(cmd.getAction())));
            message.put("business", JSON.toJSONString(emptyIfNull(cmd.getBusiness())));
            message.put("extra", JSON.toJSONString(emptyIfNull(cmd.getExtra())));
            message.put("validateStatus", finalStatus != null ? finalStatus.name() : null);
            message.put("validateErrors", JSON.toJSONString(errors));
            message.put("payload", JSON.toJSONString(cmd));

            if (sendSync) {
                trackingEventKafkaProducer.sendSync(message);
            } else {
                trackingEventKafkaProducer.send(message);
            }
        } catch (Exception e) {
            log.error("[Collect] Kafka 发送失败, requestId={}, eventCode={}", getString(cmd.getExtra(), "requestId"), getString(cmd.getAction(), "eventCode"), e);
            // 最小交付版本：Kafka 发送失败不阻断返回，仅记录日志
        }
    }

    @Override
    @Transactional
    public List<CollectResultBO> collectBatch(List<EventCollectCmd> cmds) {
        return cmds.stream().map(this::collect).toList();
    }

    /**
     * 合并四段属性用于事件属性规则校验
     */
    private Map<String, Object> mergeProperties(EventCollectCmd cmd) {
        Map<String, Object> merged = new LinkedHashMap<>();
        merged.putAll(emptyIfNull(cmd.getCommon()));
        merged.putAll(emptyIfNull(cmd.getAction()));
        merged.putAll(emptyIfNull(cmd.getBusiness()));
        merged.putAll(emptyIfNull(cmd.getExtra()));
        return merged;
    }

    /**
     * 获取字符串字段
     */
    private String getString(Map<String, Object> map, String key) {
        if (map == null || map.get(key) == null) {
            return null;
        }
        return String.valueOf(map.get(key));
    }

    /**
     * 空 Map 保护
     */
    private Map<String, Object> emptyIfNull(Map<String, Object> map) {
        return map == null ? Map.of() : map;
    }

    /**
     * 解析事件时间
     */
    private LocalDateTime parseEventTime(String eventTime, List<String> errors) {
        if (eventTime == null || eventTime.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(eventTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException ignored) {
            try {
                return LocalDateTime.parse(eventTime);
            } catch (DateTimeParseException ex) {
                errors.add("[FAIL] action.eventTime 格式错误，期望 yyyy-MM-dd HH:mm:ss");
                return null;
            }
        }
    }
}

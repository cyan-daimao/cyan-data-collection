package com.cyan.datacollection.application.collect.impl;

import com.alibaba.fastjson2.JSON;
import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
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
import com.cyan.datacollection.enums.Environment;
import com.cyan.datacollection.enums.TerminalType;
import com.cyan.datacollection.enums.ValidateStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public TrackingCollectServiceImpl(TrackingAppRepository trackingAppRepository,
                                      TrackingEventRepository trackingEventRepository,
                                      TrackingEventSampleRepository trackingEventSampleRepository,
                                      TrackingDebugSessionRepository trackingDebugSessionRepository,
                                      TrackingEventValidateService trackingEventValidateService) {
        this.trackingAppRepository = trackingAppRepository;
        this.trackingEventRepository = trackingEventRepository;
        this.trackingEventSampleRepository = trackingEventSampleRepository;
        this.trackingDebugSessionRepository = trackingDebugSessionRepository;
        this.trackingEventValidateService = trackingEventValidateService;
    }

    @Override
    @Transactional
    public CollectResultBO collect(EventCollectCmd cmd) {
        List<String> errors = new ArrayList<>();
        ValidateStatus baseStatus = ValidateStatus.PASS;

        // 校验 appCode
        TrackingApp app = trackingAppRepository.findByCode(cmd.getAppCode());
        if (app == null) {
            errors.add("[FAIL] 应用不存在: " + cmd.getAppCode());
            baseStatus = ValidateStatus.FAIL;
        } else if (app.getStatus() != AppStatus.ENABLED) {
            errors.add("[FAIL] 应用已禁用: " + cmd.getAppCode());
            baseStatus = ValidateStatus.FAIL;
        }

        // 校验 eventCode
        TrackingEvent event = trackingEventRepository.findByCode(cmd.getEventCode());
        if (event == null) {
            errors.add("[FAIL] 事件不存在: " + cmd.getEventCode());
            baseStatus = ValidateStatus.FAIL;
        }

        // 校验 eventTime
        if (cmd.getEventTime() == null) {
            errors.add("[FAIL] eventTime 不能为空");
            baseStatus = ValidateStatus.FAIL;
        }

        // 校验 terminalType
        if (cmd.getTerminalType() != null && TerminalType.of(cmd.getTerminalType()) == null) {
            errors.add("[FAIL] terminalType 不合法: " + cmd.getTerminalType());
            baseStatus = ValidateStatus.FAIL;
        }

        // 如果有 debugToken，关联 Debug 会话
        if (cmd.getDebugToken() != null && !cmd.getDebugToken().isEmpty()) {
            TrackingDebugSession session = trackingDebugSessionRepository.findByToken(cmd.getDebugToken());
            if (session == null) {
                errors.add("[FAIL] Debug Token 无效: " + cmd.getDebugToken());
                baseStatus = ValidateStatus.FAIL;
            }
        }

        // 属性规则校验（仅事件存在时执行）
        ValidateStatus propertyStatus = ValidateStatus.PASS;
        List<String> propertyErrors = new ArrayList<>();
        if (event != null) {
            ValidateResultBO result = trackingEventValidateService.validate(event, cmd.getProperties());
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

        // 写入样本
        TrackingEventSample sample = new TrackingEventSample()
                .setAppCode(cmd.getAppCode())
                .setDebugToken(cmd.getDebugToken())
                .setEventCode(cmd.getEventCode())
                .setEventTime(cmd.getEventTime())
                .setIngestionTime(LocalDateTime.now())
                .setTerminalType(cmd.getTerminalType() != null ? TerminalType.of(cmd.getTerminalType()) : null)
                .setEnvironment(cmd.getEnvironment() != null ? Environment.of(cmd.getEnvironment()) : null)
                .setUserId(cmd.getUserId())
                .setAnonymousId(cmd.getAnonymousId())
                .setSessionId(cmd.getSessionId())
                .setDeviceId(cmd.getDeviceId())
                .setSdkVersion(cmd.getSdkVersion())
                .setAppVersion(cmd.getAppVersion())
                .setPageCode(cmd.getPageCode())
                .setRequestId(cmd.getRequestId())
                .setPayload(payload)
                .setValidateStatus(finalStatus)
                .setValidateErrors(errors);

        sample = trackingEventSampleRepository.save(sample);

        return new CollectResultBO()
                .setAccepted(true)
                .setSampleId(sample.getId())
                .setValidateStatus(finalStatus)
                .setErrors(errors);
    }

    @Override
    @Transactional
    public List<CollectResultBO> collectBatch(List<EventCollectCmd> cmds) {
        return cmds.stream().map(this::collect).toList();
    }
}

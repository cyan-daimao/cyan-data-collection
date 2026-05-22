package com.cyan.datacollection.application.collect.impl;

import com.alibaba.fastjson2.JSON;
import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.collect.TrackingCollectService;
import com.cyan.datacollection.application.collect.bo.CollectResultBO;
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

    public TrackingCollectServiceImpl(TrackingAppRepository trackingAppRepository,
                                      TrackingEventRepository trackingEventRepository,
                                      TrackingEventSampleRepository trackingEventSampleRepository,
                                      TrackingDebugSessionRepository trackingDebugSessionRepository) {
        this.trackingAppRepository = trackingAppRepository;
        this.trackingEventRepository = trackingEventRepository;
        this.trackingEventSampleRepository = trackingEventSampleRepository;
        this.trackingDebugSessionRepository = trackingDebugSessionRepository;
    }

    @Override
    @Transactional
    public CollectResultBO collect(EventCollectCmd cmd) {
        List<String> errors = new ArrayList<>();

        // 校验 appCode
        TrackingApp app = trackingAppRepository.findByCode(cmd.getAppCode());
        if (app == null) {
            errors.add("应用不存在: " + cmd.getAppCode());
        } else if (app.getStatus() != AppStatus.ENABLED) {
            errors.add("应用已禁用: " + cmd.getAppCode());
        }

        // 校验 eventCode
        TrackingEvent event = trackingEventRepository.findByCode(cmd.getEventCode());
        if (event == null) {
            errors.add("事件不存在: " + cmd.getEventCode());
        }

        // 校验 eventTime
        if (cmd.getEventTime() == null) {
            errors.add("eventTime 不能为空");
        }

        // 校验 terminalType
        if (cmd.getTerminalType() != null && TerminalType.of(cmd.getTerminalType()) == null) {
            errors.add("terminalType 不合法: " + cmd.getTerminalType());
        }

        // 如果有 debugToken，关联 Debug 会话
        if (cmd.getDebugToken() != null && !cmd.getDebugToken().isEmpty()) {
            TrackingDebugSession session = trackingDebugSessionRepository.findByToken(cmd.getDebugToken());
            if (session == null) {
                errors.add("Debug Token 无效: " + cmd.getDebugToken());
            }
        }

        ValidateStatus validateStatus = errors.isEmpty() ? ValidateStatus.PASS :
                (errors.size() <= 2 ? ValidateStatus.WARN : ValidateStatus.FAIL);

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
                .setValidateStatus(validateStatus)
                .setValidateErrors(errors);

        sample = trackingEventSampleRepository.save(sample);

        return new CollectResultBO()
                .setAccepted(true)
                .setSampleId(sample.getId())
                .setValidateStatus(validateStatus)
                .setErrors(errors);
    }

    @Override
    @Transactional
    public List<CollectResultBO> collectBatch(List<EventCollectCmd> cmds) {
        return cmds.stream().map(this::collect).toList();
    }
}

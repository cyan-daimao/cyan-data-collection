package com.cyan.datacollection.application.debug.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.debug.TrackingDebugService;
import com.cyan.datacollection.application.debug.bo.DebugEventSampleBO;
import com.cyan.datacollection.application.debug.bo.DebugSessionBO;
import com.cyan.datacollection.application.debug.cmd.DebugSessionCmd;
import com.cyan.datacollection.domain.collect.TrackingEventSample;
import com.cyan.datacollection.domain.collect.query.TrackingEventSamplePageQuery;
import com.cyan.datacollection.domain.collect.repository.TrackingEventSampleRepository;
import com.cyan.datacollection.domain.debug.TrackingDebugSession;
import com.cyan.datacollection.domain.debug.repository.TrackingDebugSessionRepository;
import com.cyan.datacollection.enums.Environment;
import com.cyan.datacollection.enums.TerminalType;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Debug 控制台服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingDebugServiceImpl implements TrackingDebugService {

    private final TrackingDebugSessionRepository trackingDebugSessionRepository;
    private final TrackingEventSampleRepository trackingEventSampleRepository;

    public TrackingDebugServiceImpl(TrackingDebugSessionRepository trackingDebugSessionRepository,
                                    TrackingEventSampleRepository trackingEventSampleRepository) {
        this.trackingDebugSessionRepository = trackingDebugSessionRepository;
        this.trackingEventSampleRepository = trackingEventSampleRepository;
    }

    @Override
    @Transactional
    public DebugSessionBO createSession(DebugSessionCmd cmd) {
        TrackingDebugSession session = new TrackingDebugSession()
                .setAppCode(cmd.getAppCode())
                .setUserId(cmd.getUserId())
                .setAnonymousId(cmd.getAnonymousId())
                .setDeviceId(cmd.getDeviceId())
                .setEnvironment(cmd.getEnvironment())
                .setExpiredAt(cmd.getExpiredAt())
                .setCreateBy(cmd.getCreateBy());
        session = session.save(trackingDebugSessionRepository);
        return toDebugSessionBO(session);
    }

    @Override
    public DebugSessionBO sessionDetail(String id) {
        TrackingDebugSession session = trackingDebugSessionRepository.findById(id);
        Assert.notNull(session, new SilentException("Debug 会话不存在"));
        session.expireIfNeeded();
        return toDebugSessionBO(session);
    }

    @Override
    public List<DebugSessionBO> listActiveSessions() {
        List<TrackingDebugSession> sessions = trackingDebugSessionRepository.listActiveSessions();
        return sessions.stream().map(this::toDebugSessionBO).toList();
    }

    @Override
    public Page<DebugEventSampleBO> eventPage(TrackingEventSamplePageQuery query) {
        Page<TrackingEventSample> page = trackingEventSampleRepository.page(query);
        List<DebugEventSampleBO> list = page.getData().stream()
                .map(this::toDebugEventSampleBO)
                .toList();
        return new Page<>(list, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    public DebugEventSampleBO eventDetail(String id) {
        TrackingEventSample sample = trackingEventSampleRepository.findById(id);
        Assert.notNull(sample, new SilentException("事件样本不存在"));
        return toDebugEventSampleBO(sample);
    }

    private DebugSessionBO toDebugSessionBO(TrackingDebugSession session) {
        if (session == null) {
            return null;
        }
        return new DebugSessionBO()
                .setId(session.getId())
                .setDebugToken(session.getDebugToken())
                .setAppCode(session.getAppCode())
                .setUserId(session.getUserId())
                .setAnonymousId(session.getAnonymousId())
                .setDeviceId(session.getDeviceId())
                .setEnvironment(session.getEnvironment())
                .setExpiredAt(session.getExpiredAt())
                .setStatus(session.getStatus())
                .setCreateBy(session.getCreateBy())
                .setCreatedAt(session.getUpdatedAt()) // session DO 中没有 createdAt，用 updatedAt
                .setUpdatedAt(session.getUpdatedAt());
    }

    private DebugEventSampleBO toDebugEventSampleBO(TrackingEventSample sample) {
        if (sample == null) {
            return null;
        }
        Map<String, Object> common = parseJson(sample.getCommon());
        return new DebugEventSampleBO()
                .setId(sample.getId())
                .setAppCode(sample.getAppCode())
                .setDebugToken(sample.getDebugToken())
                .setEventCode(sample.getEventCode())
                .setEventTime(sample.getEventTime())
                .setIngestionTime(sample.getIngestionTime())
                .setTerminalType(ofTerminal(common.get("terminal_type")))
                .setEnvironment(ofEnvironment(common.get("environment")))
                .setUserId(null)
                .setAnonymousId(stringValue(common.get("anonymous_id")))
                .setSessionId(stringValue(common.get("session_id")))
                .setDeviceId(stringValue(common.get("device_id")))
                .setSdkVersion(stringValue(common.get("sdk_version")))
                .setAppVersion(stringValue(common.get("app_version")))
                .setPageCode(stringValue(common.get("page_code")))
                .setRequestId(sample.getRequestId())
                .setCommon(sample.getCommon())
                .setAction(sample.getAction())
                .setBusiness(sample.getBusiness())
                .setExtra(sample.getExtra())
                .setPayload(sample.getPayload())
                .setValidateStatus(sample.getValidateStatus())
                .setValidateErrors(sample.getValidateErrors())
                .setCreatedAt(sample.getCreatedAt());
    }

    private Map<String, Object> parseJson(String json) {
        if (json == null || json.isBlank()) {
            return Map.of();
        }
        return JSON.parseObject(json, Map.class);
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private TerminalType ofTerminal(Object value) {
        return value == null ? null : TerminalType.of(String.valueOf(value));
    }

    private Environment ofEnvironment(Object value) {
        return value == null ? null : Environment.of(String.valueOf(value));
    }
}

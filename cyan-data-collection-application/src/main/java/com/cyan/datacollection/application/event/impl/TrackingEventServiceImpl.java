package com.cyan.datacollection.application.event.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.event.TrackingEventService;
import com.cyan.datacollection.application.event.bo.TrackingEventBO;
import com.cyan.datacollection.application.event.cmd.TrackingEventCmd;
import com.cyan.datacollection.application.event.convert.TrackingEventAppConvert;
import com.cyan.datacollection.application.eventproperty.TrackingEventPropertyService;
import com.cyan.datacollection.application.eventproperty.bo.EventPropertyBO;
import com.cyan.datacollection.domain.app.TrackingApp;
import com.cyan.datacollection.domain.app.repository.TrackingAppRepository;
import com.cyan.datacollection.domain.event.TrackingEvent;
import com.cyan.datacollection.domain.event.query.TrackingEventPageQuery;
import com.cyan.datacollection.domain.event.repository.TrackingEventRepository;
import com.cyan.datacollection.enums.AppStatus;
import com.cyan.datacollection.enums.EventStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 事件定义服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingEventServiceImpl implements TrackingEventService {

    private final TrackingEventRepository trackingEventRepository;
    private final TrackingAppRepository trackingAppRepository;
    private final TrackingEventPropertyService trackingEventPropertyService;

    public TrackingEventServiceImpl(TrackingEventRepository trackingEventRepository,
                                    TrackingAppRepository trackingAppRepository,
                                    TrackingEventPropertyService trackingEventPropertyService) {
        this.trackingEventRepository = trackingEventRepository;
        this.trackingAppRepository = trackingAppRepository;
        this.trackingEventPropertyService = trackingEventPropertyService;
    }

    @Override
    public Page<TrackingEventBO> page(TrackingEventPageQuery query) {
        Page<TrackingEvent> page = trackingEventRepository.page(query);
        List<TrackingEventBO> list = page.getData().stream()
                .map(TrackingEventAppConvert.INSTANCE::toTrackingEventBO)
                .toList();
        return new Page<>(list, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    @Transactional
    public TrackingEventBO create(TrackingEventCmd cmd) {
        validateApp(cmd.getAppCode());
        TrackingEvent existing = trackingEventRepository.findByAppCodeAndCode(cmd.getAppCode(), cmd.getEventCode());
        Assert.isNull(existing, new SilentException("事件编码已存在"));

        TrackingEvent event = TrackingEventAppConvert.INSTANCE.toTrackingEvent(cmd);
        event = event.save(trackingEventRepository);
        trackingEventPropertyService.bindSystemProperties(event.getId());
        return TrackingEventAppConvert.INSTANCE.toTrackingEventBO(event);
    }

    @Override
    @Transactional
    public TrackingEventBO update(String id, TrackingEventCmd cmd) {
        TrackingEvent existing = trackingEventRepository.findById(id);
        Assert.notNull(existing, new SilentException("事件不存在"));
        Assert.isTrue(existing.getStatus() == EventStatus.DRAFT, new SilentException("只有草稿状态可编辑"));
        Assert.isTrue(existing.getAppCode() == null || existing.getAppCode().equals(cmd.getAppCode()),
                new SilentException("事件所属应用不可修改"));
        validateApp(cmd.getAppCode());

        TrackingEvent event = TrackingEventAppConvert.INSTANCE.toTrackingEvent(cmd);
        event.setId(existing.getId());
        event.setEventCode(existing.getEventCode());
        event.setCreateBy(existing.getCreateBy());
        event.setStatus(existing.getStatus());
        event.setVersion(existing.getVersion());
        event = event.update(trackingEventRepository);
        return TrackingEventAppConvert.INSTANCE.toTrackingEventBO(event);
    }

    @Override
    public TrackingEventBO detail(String id) {
        TrackingEvent event = trackingEventRepository.findById(id);
        Assert.notNull(event, new SilentException("事件不存在"));
        TrackingEventBO bo = TrackingEventAppConvert.INSTANCE.toTrackingEventBO(event);
        // 查询事件属性列表
        List<EventPropertyBO> properties = trackingEventPropertyService.listProperties(id);
        bo.setProperties(properties);
        return bo;
    }

    @Override
    @Transactional
    public TrackingEventBO publish(String id) {
        TrackingEvent event = trackingEventRepository.findById(id);
        Assert.notNull(event, new SilentException("事件不存在"));
        event = event.publish(trackingEventRepository);
        return TrackingEventAppConvert.INSTANCE.toTrackingEventBO(event);
    }

    @Override
    @Transactional
    public TrackingEventBO deprecate(String id) {
        TrackingEvent event = trackingEventRepository.findById(id);
        Assert.notNull(event, new SilentException("事件不存在"));
        event = event.deprecate(trackingEventRepository);
        return TrackingEventAppConvert.INSTANCE.toTrackingEventBO(event);
    }

    @Override
    public TrackingEventBO.UsageBO usage(String id) {
        TrackingEvent event = trackingEventRepository.findById(id);
        Assert.notNull(event, new SilentException("事件不存在"));
        // MVP 简化：返回基础统计
        TrackingEventBO.UsageBO usage = new TrackingEventBO.UsageBO();
        usage.setEventId(id);
        usage.setPlanCount(0);
        usage.setRecentSampleCount(0L);
        usage.setPropertyCount(0);
        return usage;
    }

    @Override
    public List<TrackingEventBO> findByIds(List<String> ids) {
        List<TrackingEvent> list = trackingEventRepository.findByIds(ids);
        return list.stream()
                .map(TrackingEventAppConvert.INSTANCE::toTrackingEventBO)
                .toList();
    }

    /**
     * 校验应用可用
     */
    private void validateApp(String appCode) {
        TrackingApp app = trackingAppRepository.findByCode(appCode);
        Assert.notNull(app, new SilentException("应用不存在"));
        Assert.isTrue(app.getStatus() == AppStatus.ENABLED, new SilentException("应用已禁用"));
    }
}

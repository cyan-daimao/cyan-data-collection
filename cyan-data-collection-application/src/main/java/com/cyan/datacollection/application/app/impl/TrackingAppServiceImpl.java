package com.cyan.datacollection.application.app.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.app.TrackingAppService;
import com.cyan.datacollection.application.app.bo.TrackingAppBO;
import com.cyan.datacollection.application.app.cmd.TrackingAppCmd;
import com.cyan.datacollection.application.app.convert.TrackingAppAppConvert;
import com.cyan.datacollection.domain.app.TrackingApp;
import com.cyan.datacollection.domain.app.query.TrackingAppPageQuery;
import com.cyan.datacollection.domain.app.repository.TrackingAppRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 接入应用服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingAppServiceImpl implements TrackingAppService {

    private final TrackingAppRepository trackingAppRepository;

    public TrackingAppServiceImpl(TrackingAppRepository trackingAppRepository) {
        this.trackingAppRepository = trackingAppRepository;
    }

    @Override
    public Page<TrackingAppBO> page(TrackingAppPageQuery query) {
        Page<TrackingApp> page = trackingAppRepository.page(query);
        List<TrackingAppBO> list = page.getData().stream()
                .map(TrackingAppAppConvert.INSTANCE::toTrackingAppBO)
                .toList();
        return new Page<>(list, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    @Transactional
    public TrackingAppBO create(TrackingAppCmd cmd) {
        TrackingApp existing = trackingAppRepository.findByCode(cmd.getAppCode());
        Assert.isNull(existing, new SilentException("应用编码已存在"));

        TrackingApp app = TrackingAppAppConvert.INSTANCE.toTrackingApp(cmd);
        app.setReportUrl("/api/data-collection/collect/events");
        app = app.save(trackingAppRepository);
        return TrackingAppAppConvert.INSTANCE.toTrackingAppBO(app);
    }

    @Override
    @Transactional
    public TrackingAppBO update(String id, TrackingAppCmd cmd) {
        TrackingApp existing = trackingAppRepository.findById(id);
        Assert.notNull(existing, new SilentException("应用不存在"));

        if (!existing.getAppCode().equals(cmd.getAppCode())) {
            TrackingApp codeCheck = trackingAppRepository.findByCode(cmd.getAppCode());
            Assert.isNull(codeCheck, new SilentException("应用编码已存在"));
        }

        TrackingApp app = TrackingAppAppConvert.INSTANCE.toTrackingApp(cmd);
        app.setId(existing.getId());
        app.setSecretKey(existing.getSecretKey());
        app.setReportUrl(existing.getReportUrl());
        app.setCreateBy(existing.getCreateBy());
        app.setStatus(existing.getStatus());
        app = app.update(trackingAppRepository);
        return TrackingAppAppConvert.INSTANCE.toTrackingAppBO(app);
    }

    @Override
    public TrackingAppBO detail(String id) {
        TrackingApp app = trackingAppRepository.findById(id);
        Assert.notNull(app, new SilentException("应用不存在"));
        return TrackingAppAppConvert.INSTANCE.toTrackingAppBO(app);
    }

    @Override
    @Transactional
    public TrackingAppBO rotateSecret(String id) {
        TrackingApp app = trackingAppRepository.findById(id);
        Assert.notNull(app, new SilentException("应用不存在"));
        app = app.rotateSecret(trackingAppRepository);
        return TrackingAppAppConvert.INSTANCE.toTrackingAppBO(app);
    }

    @Override
    public TrackingAppBO.IntegrationBO integrationCode(String id) {
        TrackingApp app = trackingAppRepository.findById(id);
        Assert.notNull(app, new SilentException("应用不存在"));
        TrackingAppBO.IntegrationBO integration = new TrackingAppBO.IntegrationBO();
        integration.setAppCode(app.getAppCode());
        integration.setAppName(app.getAppName());
        integration.setReportUrl(app.getReportUrl());
        integration.setJsSdkCode(generateJsSdkCode(app));
        integration.setJavaSdkCode(generateJavaSdkCode(app));
        return integration;
    }

    private String generateJsSdkCode(TrackingApp app) {
        return String.format(
                "// JavaScript SDK 示例\n" +
                "const tracker = new CyanTracker({\n" +
                "  appCode: '%s',\n" +
                "  reportUrl: '%s'\n" +
                "});\n" +
                "tracker.track('page_view', { page: 'home' });",
                app.getAppCode(), app.getReportUrl()
        );
    }

    private String generateJavaSdkCode(TrackingApp app) {
        return String.format(
                "// Java SDK 示例\n" +
                "CyanTracker tracker = CyanTracker.builder()\n" +
                "  .appCode(\"%s\")\n" +
                "  .reportUrl(\"%s\")\n" +
                "  .build();\n" +
                "tracker.track(\"order_paid\", Map.of(\"order_id\", \"O001\"));",
                app.getAppCode(), app.getReportUrl()
        );
    }
}

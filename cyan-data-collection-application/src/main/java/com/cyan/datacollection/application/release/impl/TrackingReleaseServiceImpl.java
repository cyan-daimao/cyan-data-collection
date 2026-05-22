package com.cyan.datacollection.application.release.impl;

import com.alibaba.fastjson2.JSON;
import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.release.TrackingReleaseService;
import com.cyan.datacollection.application.release.bo.TrackingReleaseBO;
import com.cyan.datacollection.application.release.cmd.TrackingReleaseCmd;
import com.cyan.datacollection.application.release.convert.TrackingReleaseAppConvert;
import com.cyan.datacollection.application.util.ReleaseCodeGenerator;
import com.cyan.datacollection.domain.event.TrackingEvent;
import com.cyan.datacollection.domain.event.repository.TrackingEventRepository;
import com.cyan.datacollection.domain.eventproperty.TrackingEventProperty;
import com.cyan.datacollection.domain.eventproperty.repository.TrackingEventPropertyRepository;
import com.cyan.datacollection.domain.plan.TrackingPlan;
import com.cyan.datacollection.domain.plan.TrackingPlanEventRelation;
import com.cyan.datacollection.domain.plan.repository.TrackingPlanEventRepository;
import com.cyan.datacollection.domain.plan.repository.TrackingPlanRepository;
import com.cyan.datacollection.domain.property.TrackingProperty;
import com.cyan.datacollection.domain.property.repository.TrackingPropertyRepository;
import com.cyan.datacollection.domain.release.TrackingRelease;
import com.cyan.datacollection.domain.release.TrackingReleaseItem;
import com.cyan.datacollection.domain.release.query.TrackingReleasePageQuery;
import com.cyan.datacollection.domain.release.repository.TrackingReleaseItemRepository;
import com.cyan.datacollection.domain.release.repository.TrackingReleaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 埋点发布版本服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingReleaseServiceImpl implements TrackingReleaseService {

    private final TrackingReleaseRepository trackingReleaseRepository;
    private final TrackingReleaseItemRepository trackingReleaseItemRepository;
    private final TrackingPlanRepository trackingPlanRepository;
    private final TrackingPlanEventRepository trackingPlanEventRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final TrackingEventPropertyRepository trackingEventPropertyRepository;
    private final TrackingPropertyRepository trackingPropertyRepository;
    private final ReleaseCodeGenerator releaseCodeGenerator;

    public TrackingReleaseServiceImpl(TrackingReleaseRepository trackingReleaseRepository,
                                      TrackingReleaseItemRepository trackingReleaseItemRepository,
                                      TrackingPlanRepository trackingPlanRepository,
                                      TrackingPlanEventRepository trackingPlanEventRepository,
                                      TrackingEventRepository trackingEventRepository,
                                      TrackingEventPropertyRepository trackingEventPropertyRepository,
                                      TrackingPropertyRepository trackingPropertyRepository,
                                      ReleaseCodeGenerator releaseCodeGenerator) {
        this.trackingReleaseRepository = trackingReleaseRepository;
        this.trackingReleaseItemRepository = trackingReleaseItemRepository;
        this.trackingPlanRepository = trackingPlanRepository;
        this.trackingPlanEventRepository = trackingPlanEventRepository;
        this.trackingEventRepository = trackingEventRepository;
        this.trackingEventPropertyRepository = trackingEventPropertyRepository;
        this.trackingPropertyRepository = trackingPropertyRepository;
        this.releaseCodeGenerator = releaseCodeGenerator;
    }

    @Override
    public Page<TrackingReleaseBO> page(TrackingReleasePageQuery query) {
        Page<TrackingRelease> page = trackingReleaseRepository.page(query);
        List<TrackingReleaseBO> list = page.getData().stream()
                .map(TrackingReleaseAppConvert.INSTANCE::toBO)
                .toList();
        return new Page<>(list, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    @Transactional
    public TrackingReleaseBO create(TrackingReleaseCmd cmd) {
        Assert.notBlank(cmd.getPlanId(), new SilentException("方案ID不能为空"));

        TrackingPlan plan = trackingPlanRepository.findById(cmd.getPlanId());
        Assert.notNull(plan, new SilentException("方案不存在"));

        // 生成发布版本号
        int version = (plan.getVersion() == null ? 1 : plan.getVersion()) + 1;

        TrackingRelease release = new TrackingRelease();
        release.setPlanId(cmd.getPlanId());
        release.setReleaseCode(releaseCodeGenerator.generate());
        release.setVersion(version);
        release.setCreateBy(cmd.getCreateBy());
        release.setUpdateBy(cmd.getUpdateBy());
        release = release.save(trackingReleaseRepository);

        // 构建发布明细
        List<TrackingReleaseItem> items = new ArrayList<>();

        // 方案快照
        Map<String, Object> planSnapshot = new LinkedHashMap<>();
        planSnapshot.put("id", plan.getId());
        planSnapshot.put("planCode", plan.getPlanCode());
        planSnapshot.put("planName", plan.getPlanName());
        planSnapshot.put("description", plan.getDescription());
        planSnapshot.put("version", plan.getVersion());
        items.add(new TrackingReleaseItem()
                .setReleaseId(release.getId())
                .setItemType("PLAN")
                .setItemId(plan.getId())
                .setItemCode(plan.getPlanCode())
                .setChangeType("ADD")
                .setSnapshot(JSON.toJSONString(planSnapshot)));

        // 查询方案内所有事件
        List<TrackingPlanEventRelation> relations = trackingPlanEventRepository.findByPlanId(cmd.getPlanId());
        for (TrackingPlanEventRelation relation : relations) {
            TrackingEvent event = trackingEventRepository.findById(relation.getEventId());
            if (event == null) {
                continue;
            }

            // 查询事件属性
            List<TrackingEventProperty> eventProperties = trackingEventPropertyRepository.findByEventId(event.getId());
            List<Map<String, Object>> propertySnapshots = new ArrayList<>();
            for (TrackingEventProperty eventProperty : eventProperties) {
                TrackingProperty property = trackingPropertyRepository.findById(eventProperty.getPropertyId());
                if (property == null) {
                    continue;
                }
                Map<String, Object> propertySnapshot = new LinkedHashMap<>();
                propertySnapshot.put("id", property.getId());
                propertySnapshot.put("propertyCode", property.getPropertyCode());
                propertySnapshot.put("propertyName", property.getPropertyName());
                propertySnapshot.put("propertyType", property.getPropertyType() != null ? property.getPropertyType().name() : null);
                propertySnapshot.put("dataType", property.getDataType() != null ? property.getDataType().name() : null);
                propertySnapshot.put("isRequired", eventProperty.getIsRequired());
                propertySnapshot.put("description", property.getDescription());
                propertySnapshots.add(propertySnapshot);
            }

            Map<String, Object> eventSnapshot = new LinkedHashMap<>();
            eventSnapshot.put("id", event.getId());
            eventSnapshot.put("eventCode", event.getEventCode());
            eventSnapshot.put("eventName", event.getEventName());
            eventSnapshot.put("eventType", event.getEventType() != null ? event.getEventType().name() : null);
            eventSnapshot.put("description", event.getDescription());
            eventSnapshot.put("triggerTiming", event.getTriggerTiming());
            eventSnapshot.put("terminalTypes", event.getTerminalTypes());
            eventSnapshot.put("isCore", event.getIsCore());
            eventSnapshot.put("properties", propertySnapshots);

            items.add(new TrackingReleaseItem()
                    .setReleaseId(release.getId())
                    .setItemType("EVENT")
                    .setItemId(event.getId())
                    .setItemCode(event.getEventCode())
                    .setChangeType("ADD")
                    .setSnapshot(JSON.toJSONString(eventSnapshot)));
        }

        if (!items.isEmpty()) {
            trackingReleaseItemRepository.saveBatch(items);
        }

        return detail(release.getId());
    }

    @Override
    public TrackingReleaseBO detail(String id) {
        TrackingRelease release = trackingReleaseRepository.findById(id);
        Assert.notNull(release, new SilentException("发布版本不存在"));

        TrackingReleaseBO bo = TrackingReleaseAppConvert.INSTANCE.toBO(release);

        List<TrackingReleaseItem> items = trackingReleaseItemRepository.findByReleaseId(id);
        List<TrackingReleaseBO.ItemBO> itemBOs = Optional.ofNullable(items).orElse(List.of()).stream()
                .map(item -> new TrackingReleaseBO.ItemBO()
                        .setId(item.getId())
                        .setReleaseId(item.getReleaseId())
                        .setItemType(item.getItemType())
                        .setItemId(item.getItemId())
                        .setItemCode(item.getItemCode())
                        .setChangeType(item.getChangeType())
                        .setSnapshot(item.getSnapshot())
                        .setCreatedAt(item.getCreatedAt()))
                .toList();
        bo.setItems(itemBOs);

        return bo;
    }

    @Override
    public List<TrackingReleaseBO.ItemBO> diff(String id) {
        // MVP：返回当前发布的明细列表
        TrackingReleaseBO bo = detail(id);
        return bo.getItems();
    }

    @Override
    @Transactional
    public TrackingReleaseBO submit(String id) {
        TrackingRelease release = trackingReleaseRepository.findById(id);
        Assert.notNull(release, new SilentException("发布版本不存在"));
        release = release.submit(trackingReleaseRepository);
        return TrackingReleaseAppConvert.INSTANCE.toBO(release);
    }

    @Override
    @Transactional
    public TrackingReleaseBO publish(String id) {
        TrackingRelease release = trackingReleaseRepository.findById(id);
        Assert.notNull(release, new SilentException("发布版本不存在"));

        TrackingPlan plan = trackingPlanRepository.findById(release.getPlanId());
        Assert.notNull(plan, new SilentException("方案不存在"));

        // 更新 release 状态为 PUBLISHED
        release = release.publish(trackingReleaseRepository);

        // 更新 plan 状态为 PUBLISHED 并设置 publishedVersionId
        plan.publish(trackingPlanRepository, release.getId());

        return TrackingReleaseAppConvert.INSTANCE.toBO(release);
    }
}

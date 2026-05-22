package com.cyan.datacollection.application.plan.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.event.TrackingEventService;
import com.cyan.datacollection.application.plan.TrackingPlanService;
import com.cyan.datacollection.application.plan.bo.TrackingPlanBO;
import com.cyan.datacollection.application.plan.cmd.TrackingPlanCmd;
import com.cyan.datacollection.application.plan.cmd.TrackingPlanEventConfigCmd;
import com.cyan.datacollection.application.plan.convert.TrackingPlanAppConvert;
import com.cyan.datacollection.application.util.PlanCodeGenerator;
import com.cyan.datacollection.domain.event.TrackingEvent;
import com.cyan.datacollection.domain.event.repository.TrackingEventRepository;
import com.cyan.datacollection.domain.plan.TrackingPlan;
import com.cyan.datacollection.domain.plan.TrackingPlanEventRelation;
import com.cyan.datacollection.domain.plan.repository.TrackingPlanEventRepository;
import com.cyan.datacollection.domain.plan.query.TrackingPlanPageQuery;
import com.cyan.datacollection.domain.plan.repository.TrackingPlanRepository;
import com.cyan.datacollection.enums.PlanStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 埋点方案服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingPlanServiceImpl implements TrackingPlanService {

    private final TrackingPlanRepository trackingPlanRepository;
    private final TrackingPlanEventRepository trackingPlanEventRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final PlanCodeGenerator planCodeGenerator;
    private final TrackingEventService trackingEventService;

    public TrackingPlanServiceImpl(TrackingPlanRepository trackingPlanRepository,
                                   TrackingPlanEventRepository trackingPlanEventRepository,
                                   TrackingEventRepository trackingEventRepository,
                                   PlanCodeGenerator planCodeGenerator,
                                   TrackingEventService trackingEventService) {
        this.trackingPlanRepository = trackingPlanRepository;
        this.trackingPlanEventRepository = trackingPlanEventRepository;
        this.trackingEventRepository = trackingEventRepository;
        this.planCodeGenerator = planCodeGenerator;
        this.trackingEventService = trackingEventService;
    }

    @Override
    public Page<TrackingPlanBO> page(TrackingPlanPageQuery query) {
        Page<TrackingPlan> page = trackingPlanRepository.page(query);
        List<TrackingPlanBO> list = page.getData().stream()
                .map(TrackingPlanAppConvert.INSTANCE::toBO)
                .toList();
        return new Page<>(list, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    @Transactional
    public TrackingPlanBO create(TrackingPlanCmd cmd) {
        TrackingPlan plan = TrackingPlanAppConvert.INSTANCE.toDomain(cmd);
        plan.setPlanCode(planCodeGenerator.generate());
        plan = plan.save(trackingPlanRepository);
        return TrackingPlanAppConvert.INSTANCE.toBO(plan);
    }

    @Override
    @Transactional
    public TrackingPlanBO update(String id, TrackingPlanCmd cmd) {
        TrackingPlan existing = trackingPlanRepository.findById(id);
        Assert.notNull(existing, new SilentException("方案不存在"));

        TrackingPlan plan = TrackingPlanAppConvert.INSTANCE.toDomain(cmd);
        plan.setId(existing.getId());
        plan.setPlanCode(existing.getPlanCode());
        plan.setCreateBy(existing.getCreateBy());
        plan.setStatus(existing.getStatus());
        plan.setVersion(existing.getVersion());
        plan = plan.update(trackingPlanRepository);
        return TrackingPlanAppConvert.INSTANCE.toBO(plan);
    }

    @Override
    public TrackingPlanBO detail(String id) {
        TrackingPlan plan = trackingPlanRepository.findById(id);
        Assert.notNull(plan, new SilentException("方案不存在"));

        TrackingPlanBO bo = TrackingPlanAppConvert.INSTANCE.toBO(plan);

        // 查询方案事件关系
        List<TrackingPlanEventRelation> relations = trackingPlanEventRepository.findByPlanId(id);
        List<TrackingPlanBO.PlanEventBO> eventBOs = new ArrayList<>();
        for (TrackingPlanEventRelation relation : relations) {
            TrackingEvent event = trackingEventRepository.findById(relation.getEventId());
            if (event != null) {
                eventBOs.add(new TrackingPlanBO.PlanEventBO()
                        .setEventId(event.getId())
                        .setEventCode(event.getEventCode())
                        .setEventName(event.getEventName())
                        .setEventType(event.getEventType() != null ? event.getEventType().name() : null)
                        .setIsRequired(relation.getIsRequired()));
            }
        }
        bo.setEvents(eventBOs);
        return bo;
    }

    @Override
    @Transactional
    public void addEvent(String id, List<String> eventIds) {
        TrackingPlan plan = trackingPlanRepository.findById(id);
        Assert.notNull(plan, new SilentException("方案不存在"));

        // 校验方案状态可编辑
        Assert.isTrue(plan.getStatus() == PlanStatus.DRAFT
                        || plan.getStatus() == PlanStatus.REVIEWING
                        || plan.getStatus() == PlanStatus.DEVELOPING,
                new SilentException("当前方案状态不允许编辑事件"));

        // 验证事件存在性
        if (eventIds != null && !eventIds.isEmpty()) {
            trackingEventService.findByIds(eventIds);
        }

        // 逐个添加事件，跳过已存在的
        for (String eventId : eventIds) {
            if (trackingPlanEventRepository.existsByPlanIdAndEventId(id, eventId)) {
                throw new SilentException("事件已在方案中: " + eventId);
            }
            TrackingPlanEventRelation relation = new TrackingPlanEventRelation();
            relation.setPlanId(id);
            relation.setEventId(eventId);
            relation.setIsRequired(false);
            relation.save();
            trackingPlanEventRepository.save(relation);
        }
    }

    @Override
    @Transactional
    public void removeEvent(String id, String eventId) {
        TrackingPlan plan = trackingPlanRepository.findById(id);
        Assert.notNull(plan, new SilentException("方案不存在"));

        // 校验方案状态可编辑
        Assert.isTrue(plan.getStatus() == PlanStatus.DRAFT
                        || plan.getStatus() == PlanStatus.REVIEWING
                        || plan.getStatus() == PlanStatus.DEVELOPING,
                new SilentException("当前方案状态不允许编辑事件"));

        trackingPlanEventRepository.deleteByPlanIdAndEventId(id, eventId);
    }

    @Override
    @Transactional
    public TrackingPlanBO submitReview(String id) {
        TrackingPlan plan = trackingPlanRepository.findById(id);
        Assert.notNull(plan, new SilentException("方案不存在"));
        plan = plan.submitReview(trackingPlanRepository);
        return TrackingPlanAppConvert.INSTANCE.toBO(plan);
    }

    @Override
    @Transactional
    public TrackingPlanBO approve(String id) {
        TrackingPlan plan = trackingPlanRepository.findById(id);
        Assert.notNull(plan, new SilentException("方案不存在"));
        plan = plan.approve(trackingPlanRepository);
        return TrackingPlanAppConvert.INSTANCE.toBO(plan);
    }

    @Override
    @Transactional
    public TrackingPlanBO reject(String id) {
        TrackingPlan plan = trackingPlanRepository.findById(id);
        Assert.notNull(plan, new SilentException("方案不存在"));
        plan = plan.reject(trackingPlanRepository);
        return TrackingPlanAppConvert.INSTANCE.toBO(plan);
    }

    @Override
    @Transactional
    public void configEventProperties(String id, String eventId, List<TrackingPlanEventConfigCmd> cmds) {
        throw new SilentException("暂不支持方案级属性配置，请在事件维度管理属性");
    }
}

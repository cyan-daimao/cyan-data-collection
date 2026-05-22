package com.cyan.datacollection.application.plan.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.event.TrackingEventService;
import com.cyan.datacollection.application.plan.TrackingPlanService;
import com.cyan.datacollection.application.plan.bo.TrackingPlanBO;
import com.cyan.datacollection.application.plan.cmd.TrackingPlanCmd;
import com.cyan.datacollection.application.plan.convert.TrackingPlanAppConvert;
import com.cyan.datacollection.application.util.PlanCodeGenerator;
import com.cyan.datacollection.domain.plan.TrackingPlan;
import com.cyan.datacollection.domain.plan.query.TrackingPlanPageQuery;
import com.cyan.datacollection.domain.plan.repository.TrackingPlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PlanCodeGenerator planCodeGenerator;
    private final TrackingEventService trackingEventService;

    public TrackingPlanServiceImpl(TrackingPlanRepository trackingPlanRepository,
                                   PlanCodeGenerator planCodeGenerator,
                                   TrackingEventService trackingEventService) {
        this.trackingPlanRepository = trackingPlanRepository;
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
        // MVP 简化：事件列表暂不持久化，返回空列表
        TrackingPlanBO bo = TrackingPlanAppConvert.INSTANCE.toBO(plan);
        bo.setEvents(List.of());
        return bo;
    }

    @Override
    @Transactional
    public void addEvent(String id, List<String> eventIds) {
        TrackingPlan plan = trackingPlanRepository.findById(id);
        Assert.notNull(plan, new SilentException("方案不存在"));

        // 验证事件存在性
        if (eventIds != null && !eventIds.isEmpty()) {
            trackingEventService.findByIds(eventIds);
        }

        // MVP 简化：方案-事件关系表待补充，此处仅做存在性校验
        log.info("添加事件到方案: planId={}, eventIds={}", id, eventIds);
    }

    @Override
    @Transactional
    public void removeEvent(String id, String eventId) {
        TrackingPlan plan = trackingPlanRepository.findById(id);
        Assert.notNull(plan, new SilentException("方案不存在"));

        // MVP 简化：方案-事件关系表待补充，此处仅做存在性校验
        log.info("从方案移除事件: planId={}, eventId={}", id, eventId);
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
    public void configEventProperties(String id, String eventId, List<com.cyan.datacollection.application.plan.cmd.TrackingPlanEventConfigCmd> cmds) {
        TrackingPlan plan = trackingPlanRepository.findById(id);
        Assert.notNull(plan, new SilentException("方案不存在"));
        // MVP 简化：事件属性配置暂由事件模块独立管理
        log.info("配置方案事件属性: planId={}, eventId={}", id, eventId);
    }
}

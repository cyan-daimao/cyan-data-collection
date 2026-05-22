package com.cyan.datacollection.application.acceptance.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.acceptance.TrackingAcceptanceService;
import com.cyan.datacollection.application.acceptance.bo.TrackingAcceptanceResultBO;
import com.cyan.datacollection.application.acceptance.bo.TrackingAcceptanceTaskBO;
import com.cyan.datacollection.application.acceptance.cmd.TrackingAcceptanceTaskCmd;
import com.cyan.datacollection.application.acceptance.convert.TrackingAcceptanceAppConvert;
import com.cyan.datacollection.application.util.AcceptanceTaskCodeGenerator;
import com.cyan.datacollection.domain.acceptance.TrackingAcceptanceResult;
import com.cyan.datacollection.domain.acceptance.TrackingAcceptanceTask;
import com.cyan.datacollection.domain.acceptance.query.TrackingAcceptanceTaskPageQuery;
import com.cyan.datacollection.domain.acceptance.repository.TrackingAcceptanceResultRepository;
import com.cyan.datacollection.domain.acceptance.repository.TrackingAcceptanceTaskRepository;
import com.cyan.datacollection.domain.collect.TrackingEventSample;
import com.cyan.datacollection.domain.collect.repository.TrackingEventSampleRepository;
import com.cyan.datacollection.domain.event.TrackingEvent;
import com.cyan.datacollection.domain.event.repository.TrackingEventRepository;
import com.cyan.datacollection.domain.plan.TrackingPlanEventRelation;
import com.cyan.datacollection.domain.plan.repository.TrackingPlanEventRepository;
import com.cyan.datacollection.enums.AcceptanceStatus;
import com.cyan.datacollection.enums.ValidateStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 验收任务服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingAcceptanceServiceImpl implements TrackingAcceptanceService {

    private final TrackingAcceptanceTaskRepository trackingAcceptanceTaskRepository;
    private final TrackingAcceptanceResultRepository trackingAcceptanceResultRepository;
    private final TrackingPlanEventRepository trackingPlanEventRepository;
    private final TrackingEventSampleRepository trackingEventSampleRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final AcceptanceTaskCodeGenerator acceptanceTaskCodeGenerator;

    public TrackingAcceptanceServiceImpl(TrackingAcceptanceTaskRepository trackingAcceptanceTaskRepository,
                                         TrackingAcceptanceResultRepository trackingAcceptanceResultRepository,
                                         TrackingPlanEventRepository trackingPlanEventRepository,
                                         TrackingEventSampleRepository trackingEventSampleRepository,
                                         TrackingEventRepository trackingEventRepository,
                                         AcceptanceTaskCodeGenerator acceptanceTaskCodeGenerator) {
        this.trackingAcceptanceTaskRepository = trackingAcceptanceTaskRepository;
        this.trackingAcceptanceResultRepository = trackingAcceptanceResultRepository;
        this.trackingPlanEventRepository = trackingPlanEventRepository;
        this.trackingEventSampleRepository = trackingEventSampleRepository;
        this.trackingEventRepository = trackingEventRepository;
        this.acceptanceTaskCodeGenerator = acceptanceTaskCodeGenerator;
    }

    @Override
    public Page<TrackingAcceptanceTaskBO> page(TrackingAcceptanceTaskPageQuery query) {
        Page<TrackingAcceptanceTask> page = trackingAcceptanceTaskRepository.page(query);
        List<TrackingAcceptanceTaskBO> list = page.getData().stream()
                .map(TrackingAcceptanceAppConvert.INSTANCE::toTaskBO)
                .toList();
        return new Page<>(list, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    @Transactional
    public TrackingAcceptanceTaskBO create(TrackingAcceptanceTaskCmd cmd) {
        TrackingAcceptanceTask task = TrackingAcceptanceAppConvert.INSTANCE.toTaskDomain(cmd);
        task.setTaskCode(acceptanceTaskCodeGenerator.generate());
        task = task.save(trackingAcceptanceTaskRepository);
        return TrackingAcceptanceAppConvert.INSTANCE.toTaskBO(task);
    }

    @Override
    public TrackingAcceptanceTaskBO detail(String id) {
        TrackingAcceptanceTask task = trackingAcceptanceTaskRepository.findById(id);
        Assert.notNull(task, new SilentException("验收任务不存在"));

        TrackingAcceptanceTaskBO bo = TrackingAcceptanceAppConvert.INSTANCE.toTaskBO(task);

        List<TrackingAcceptanceResult> results = trackingAcceptanceResultRepository.findByTaskId(id);
        List<TrackingAcceptanceResultBO> resultBOs = results.stream()
                .map(TrackingAcceptanceAppConvert.INSTANCE::toResultBO)
                .toList();
        bo.setResults(resultBOs);
        return bo;
    }

    @Override
    @Transactional
    public TrackingAcceptanceTaskBO run(String id) {
        TrackingAcceptanceTask task = trackingAcceptanceTaskRepository.findById(id);
        Assert.notNull(task, new SilentException("验收任务不存在"));

        if (task.getStatus() == AcceptanceStatus.RUNNING) {
            throw new SilentException("验收任务正在执行中");
        }

        // 更新状态为执行中
        task.setStatus(AcceptanceStatus.RUNNING);
        task = task.update(trackingAcceptanceTaskRepository);

        // 1. 查询方案内事件列表
        List<TrackingPlanEventRelation> planEvents = trackingPlanEventRepository.findByPlanId(task.getPlanId());

        // 2. 查询所有样本
        List<TrackingEventSample> samples = trackingEventSampleRepository.findByDebugToken(task.getDebugToken());

        // 按事件编码分组样本
        Map<String, List<TrackingEventSample>> samplesByEventCode = samples.stream()
                .collect(Collectors.groupingBy(TrackingEventSample::getEventCode));

        // 批量查询事件定义，建立 eventId -> eventCode 映射
        List<String> eventIds = planEvents.stream()
                .map(TrackingPlanEventRelation::getEventId)
                .distinct()
                .toList();
        Map<String, String> eventIdToCodeMap;
        if (!eventIds.isEmpty()) {
            List<TrackingEvent> events = trackingEventRepository.findByIds(eventIds);
            eventIdToCodeMap = events.stream()
                    .collect(Collectors.toMap(TrackingEvent::getId, TrackingEvent::getEventCode, (a, b) -> a));
        } else {
            eventIdToCodeMap = Map.of();
        }

        // 3. 对每个方案内事件生成验收结果
        List<TrackingAcceptanceResult> results = new ArrayList<>();
        int coveredEventCount = 0;
        int totalPassSampleCount = 0;
        int noErrorPassSampleCount = 0;

        for (TrackingPlanEventRelation planEvent : planEvents) {
            String eventId = planEvent.getEventId();
            String eventCode = eventIdToCodeMap.get(eventId);
            if (eventCode == null) {
                // 未找到对应事件编码，标记为失败
                results.add(new TrackingAcceptanceResult()
                        .setTaskId(id)
                        .setEventId(eventId)
                        .setEventCode("")
                        .setStatus("FAIL")
                        .setErrorItems(List.of("未找到事件定义"))
                        .setSampleIds(List.of()));
                continue;
            }

            List<TrackingEventSample> eventSamples = samplesByEventCode.getOrDefault(eventCode, List.of());
            List<TrackingEventSample> passSamples = eventSamples.stream()
                    .filter(s -> s.getValidateStatus() == ValidateStatus.PASS)
                    .toList();

            boolean hasPass = !passSamples.isEmpty();
            if (hasPass) {
                coveredEventCount++;
            }

            List<String> sampleIds = passSamples.stream()
                    .map(TrackingEventSample::getId)
                    .toList();

            List<String> errorItems = new ArrayList<>();
            if (!hasPass) {
                if (eventSamples.isEmpty()) {
                    errorItems.add("未找到样本数据");
                } else {
                    errorItems.add("无校验通过样本");
                }
            }

            for (TrackingEventSample passSample : passSamples) {
                totalPassSampleCount++;
                List<String> errors = passSample.getValidateErrors();
                if (errors == null || errors.isEmpty()) {
                    noErrorPassSampleCount++;
                }
            }

            results.add(new TrackingAcceptanceResult()
                    .setTaskId(id)
                    .setEventId(eventId)
                    .setEventCode(eventCode)
                    .setStatus(hasPass ? "PASS" : "FAIL")
                    .setErrorItems(errorItems)
                    .setSampleIds(sampleIds));
        }

        // 4. 统计指标
        int totalEventCount = planEvents.size();
        BigDecimal eventCoverageRate = totalEventCount > 0
                ? BigDecimal.valueOf(coveredEventCount).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(totalEventCount), 4, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        BigDecimal requiredPropertyCompleteRate = totalPassSampleCount > 0
                ? BigDecimal.valueOf(noErrorPassSampleCount).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(totalPassSampleCount), 4, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        BigDecimal typeValidRate = requiredPropertyCompleteRate;

        // 5. 保存验收结果
        trackingAcceptanceResultRepository.deleteByTaskId(id);
        if (!results.isEmpty()) {
            TrackingAcceptanceResult.saveBatch(trackingAcceptanceResultRepository, results);
        }

        // 6. 更新任务状态和统计
        AcceptanceStatus finalStatus = coveredEventCount >= totalEventCount && totalEventCount > 0
                ? AcceptanceStatus.PASS
                : AcceptanceStatus.FAIL;
        task.setStatus(finalStatus);
        task.setEventCoverageRate(eventCoverageRate);
        task.setRequiredPropertyCompleteRate(requiredPropertyCompleteRate);
        task.setTypeValidRate(typeValidRate);
        task = task.update(trackingAcceptanceTaskRepository);

        return TrackingAcceptanceAppConvert.INSTANCE.toTaskBO(task);
    }

    @Override
    @Transactional
    public TrackingAcceptanceTaskBO approve(String id) {
        TrackingAcceptanceTask task = trackingAcceptanceTaskRepository.findById(id);
        Assert.notNull(task, new SilentException("验收任务不存在"));
        task = task.approve(trackingAcceptanceTaskRepository);
        return TrackingAcceptanceAppConvert.INSTANCE.toTaskBO(task);
    }

    @Override
    @Transactional
    public TrackingAcceptanceTaskBO reject(String id) {
        TrackingAcceptanceTask task = trackingAcceptanceTaskRepository.findById(id);
        Assert.notNull(task, new SilentException("验收任务不存在"));
        task = task.reject(trackingAcceptanceTaskRepository);
        return TrackingAcceptanceAppConvert.INSTANCE.toTaskBO(task);
    }
}

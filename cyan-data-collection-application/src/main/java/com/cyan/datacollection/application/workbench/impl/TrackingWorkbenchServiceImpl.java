package com.cyan.datacollection.application.workbench.impl;

import com.cyan.datacollection.application.workbench.TrackingWorkbenchService;
import com.cyan.datacollection.application.workbench.bo.WorkbenchQualityRiskBO;
import com.cyan.datacollection.application.workbench.bo.WorkbenchSummaryBO;
import com.cyan.datacollection.application.workbench.bo.WorkbenchTodoBO;
import com.cyan.datacollection.domain.quality.TrackingAlert;
import com.cyan.datacollection.domain.quality.repository.TrackingAlertRepository;
import com.cyan.datacollection.infra.persistence.workbench.mappers.TrackingWorkbenchMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作台服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingWorkbenchServiceImpl implements TrackingWorkbenchService {

    private final TrackingWorkbenchMapper trackingWorkbenchMapper;
    private final TrackingAlertRepository trackingAlertRepository;

    public TrackingWorkbenchServiceImpl(TrackingWorkbenchMapper trackingWorkbenchMapper,
                                        TrackingAlertRepository trackingAlertRepository) {
        this.trackingWorkbenchMapper = trackingWorkbenchMapper;
        this.trackingAlertRepository = trackingAlertRepository;
    }

    @Override
    public WorkbenchSummaryBO summary() {
        WorkbenchSummaryBO bo = new WorkbenchSummaryBO();
        bo.setEventCount(trackingWorkbenchMapper.countEvents());
        bo.setPropertyCount(trackingWorkbenchMapper.countProperties());
        bo.setPlanCount(trackingWorkbenchMapper.countPlans());
        bo.setTodaySampleCount(trackingWorkbenchMapper.countTodaySamples());
        bo.setTodayFailSampleCount(trackingWorkbenchMapper.countTodayFailSamples());
        bo.setReviewingPlanCount(trackingWorkbenchMapper.countReviewingPlans());
        bo.setPendingTaskCount(trackingWorkbenchMapper.countPendingTasks());
        bo.setOpenAlertCount(trackingAlertRepository.countByStatus(
                com.cyan.datacollection.enums.AlertStatus.OPEN));
        return bo;
    }

    @Override
    public List<WorkbenchTodoBO> todos() {
        List<WorkbenchTodoBO> list = new ArrayList<>();
        List<WorkbenchTodoBO> plans = trackingWorkbenchMapper.selectReviewingPlans();
        if (plans != null) {
            for (WorkbenchTodoBO plan : plans) {
                plan.setTodoType("PLAN");
                list.add(plan);
            }
        }
        List<WorkbenchTodoBO> tasks = trackingWorkbenchMapper.selectPendingTasks();
        if (tasks != null) {
            for (WorkbenchTodoBO task : tasks) {
                task.setTodoType("TASK");
                list.add(task);
            }
        }
        return list;
    }

    @Override
    public List<WorkbenchQualityRiskBO> qualityRisks() {
        List<TrackingAlert> alerts = trackingAlertRepository.findOpenByLevel("ERROR");
        return alerts.stream().map(alert -> new WorkbenchQualityRiskBO()
                .setId(alert.getId())
                .setAlertType(alert.getAlertType() != null ? alert.getAlertType().name() : null)
                .setAppCode(alert.getAppCode())
                .setEventCode(alert.getEventCode())
                .setAlertLevel(alert.getAlertLevel() != null ? alert.getAlertLevel().name() : null)
                .setAlertMessage(alert.getAlertMessage())
                .setTriggeredAt(alert.getTriggeredAt())
        ).toList();
    }
}

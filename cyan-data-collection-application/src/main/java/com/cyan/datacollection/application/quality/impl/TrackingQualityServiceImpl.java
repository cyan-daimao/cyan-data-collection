package com.cyan.datacollection.application.quality.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.quality.TrackingQualityService;
import com.cyan.datacollection.application.quality.bo.QualityOverviewBO;
import com.cyan.datacollection.application.quality.bo.QualityTrendBO;
import com.cyan.datacollection.application.quality.cmd.QualityOverviewQuery;
import com.cyan.datacollection.application.quality.cmd.QualityTrendQuery;

import com.cyan.datacollection.domain.quality.query.TrackingAlertPageQuery;
import com.cyan.datacollection.domain.quality.TrackingAlert;
import com.cyan.datacollection.domain.quality.repository.TrackingAlertRepository;
import com.cyan.datacollection.enums.AlertLevel;
import com.cyan.datacollection.enums.AlertStatus;
import com.cyan.datacollection.enums.AlertType;
import com.cyan.datacollection.infra.persistence.collect.mappers.TrackingEventSampleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 质量监控服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingQualityServiceImpl implements TrackingQualityService {

    private final TrackingEventSampleMapper trackingEventSampleMapper;
    private final TrackingAlertRepository trackingAlertRepository;

    public TrackingQualityServiceImpl(TrackingEventSampleMapper trackingEventSampleMapper,
                                      TrackingAlertRepository trackingAlertRepository) {
        this.trackingEventSampleMapper = trackingEventSampleMapper;
        this.trackingAlertRepository = trackingAlertRepository;
    }

    @Override
    public List<QualityOverviewBO> eventsOverview(QualityOverviewQuery query) {
        List<QualityOverviewBO> list = trackingEventSampleMapper.aggregateToday(
                query.getAppCode(), query.getEventCode());
        for (QualityOverviewBO bo : list) {
            if (bo.getTotalCount() != null && bo.getTotalCount() > 0) {
                BigDecimal passRate = BigDecimal.valueOf(bo.getPassCount())
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(bo.getTotalCount()), 2, RoundingMode.HALF_UP);
                bo.setPassRate(passRate);
            } else {
                bo.setPassRate(BigDecimal.ZERO);
            }
            // 自动告警：失败率超过20%
            if (bo.getTotalCount() != null && bo.getTotalCount() > 0) {
                double failRate = (double) bo.getFailCount() / bo.getTotalCount();
                if (failRate > 0.2) {
                    generateFailRateAlertIfNotExists(bo.getAppCode(), bo.getEventCode(), failRate);
                }
            }
            // 自动告警：最近1小时无数据
            Long recentCount = trackingEventSampleMapper.countRecentByAppCodeAndEventCode(
                    bo.getAppCode(), bo.getEventCode(), LocalDateTime.now().minusHours(1));
            if (recentCount == null || recentCount == 0) {
                generateNoDataAlertIfNotExists(bo.getAppCode(), bo.getEventCode());
            }
        }
        return list;
    }

    @Override
    public List<QualityTrendBO> eventsTrend(QualityTrendQuery query) {
        LocalDateTime start = query.getStartTime() != null ? query.getStartTime() : LocalDateTime.now().minusHours(24);
        LocalDateTime end = query.getEndTime() != null ? query.getEndTime() : LocalDateTime.now();
        return trackingEventSampleMapper.trendByHour(
                query.getAppCode(), query.getEventCode(), start, end);
    }

    @Override
    public Page<TrackingAlert> alertPage(TrackingAlertPageQuery query) {
        return trackingAlertRepository.page(query);
    }

    @Override
    @Transactional
    public TrackingAlert closeAlert(String id) {
        TrackingAlert alert = trackingAlertRepository.findById(id);
        Assert.notNull(alert, new SilentException("告警不存在"));
        return alert.close(trackingAlertRepository);
    }

    /**
     * 生成失败率过高告警（如果不存在未关闭的同类告警）
     */
    private void generateFailRateAlertIfNotExists(String appCode, String eventCode, double failRate) {
        List<TrackingAlert> exists = trackingAlertRepository.findOpenByAppCodeAndEventCodeAndType(
                appCode, eventCode, AlertType.FAIL_RATE_HIGH);
        if (exists != null && !exists.isEmpty()) {
            return;
        }
        TrackingAlert alert = new TrackingAlert();
        alert.setAlertType(AlertType.FAIL_RATE_HIGH);
        alert.setAppCode(appCode);
        alert.setEventCode(eventCode);
        alert.setAlertLevel(AlertLevel.ERROR);
        alert.setAlertMessage(String.format("事件 %s 失败率 %.2f%%，超过阈值 20%%", eventCode, failRate * 100));
        alert.save(trackingAlertRepository);
    }

    /**
     * 生成无数据告警（如果不存在未关闭的同类告警）
     */
    private void generateNoDataAlertIfNotExists(String appCode, String eventCode) {
        List<TrackingAlert> exists = trackingAlertRepository.findOpenByAppCodeAndEventCodeAndType(
                appCode, eventCode, AlertType.NO_DATA);
        if (exists != null && !exists.isEmpty()) {
            return;
        }
        TrackingAlert alert = new TrackingAlert();
        alert.setAlertType(AlertType.NO_DATA);
        alert.setAppCode(appCode);
        alert.setEventCode(eventCode);
        alert.setAlertLevel(AlertLevel.WARN);
        alert.setAlertMessage(String.format("事件 %s 最近1小时无上报数据", eventCode));
        alert.save(trackingAlertRepository);
    }
}

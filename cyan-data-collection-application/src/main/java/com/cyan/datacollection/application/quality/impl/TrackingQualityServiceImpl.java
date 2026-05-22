package com.cyan.datacollection.application.quality.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.quality.TrackingQualityService;
import com.cyan.datacollection.application.quality.bo.QualityOverviewBO;
import com.cyan.datacollection.application.quality.bo.QualityTrendBO;
import com.cyan.datacollection.application.quality.cmd.QualityOverviewQuery;
import com.cyan.datacollection.application.quality.cmd.QualityTrendQuery;

import com.cyan.datacollection.domain.quality.TrackingAlert;
import com.cyan.datacollection.domain.quality.query.TrackingAlertPageQuery;
import com.cyan.datacollection.domain.quality.repository.TrackingAlertRepository;
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
}

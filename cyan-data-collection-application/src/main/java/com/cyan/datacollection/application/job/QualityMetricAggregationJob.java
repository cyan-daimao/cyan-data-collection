package com.cyan.datacollection.application.job;

import com.cyan.datacollection.application.quality.bo.QualityOverviewBO;
import com.cyan.datacollection.domain.quality.TrackingQualityMetric;
import com.cyan.datacollection.domain.quality.repository.TrackingQualityMetricRepository;
import com.cyan.datacollection.infra.persistence.collect.mappers.TrackingEventSampleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 质量指标聚合定时任务
 * 每小时聚合样本数据写入 tracking_quality_metric
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Component
public class QualityMetricAggregationJob {

    private final TrackingEventSampleMapper trackingEventSampleMapper;
    private final TrackingQualityMetricRepository trackingQualityMetricRepository;

    public QualityMetricAggregationJob(TrackingEventSampleMapper trackingEventSampleMapper,
                                       TrackingQualityMetricRepository trackingQualityMetricRepository) {
        this.trackingEventSampleMapper = trackingEventSampleMapper;
        this.trackingQualityMetricRepository = trackingQualityMetricRepository;
    }

    /**
     * 每小时执行一次，聚合上一小时的数据
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void aggregateHourly() {
        log.info("[Job] 开始执行质量指标小时聚合");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourStart = now.minusHours(1).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime hourEnd = hourStart.plusHours(1).minusSeconds(1);

        List<QualityOverviewBO> list = trackingEventSampleMapper.aggregateByTimeRange(null, null, hourStart, hourEnd);
        int count = 0;
        for (QualityOverviewBO bo : list) {
            BigDecimal passRate = BigDecimal.ZERO;
            if (bo.getTotalCount() != null && bo.getTotalCount() > 0) {
                passRate = BigDecimal.valueOf(bo.getPassCount())
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(bo.getTotalCount()), 2, RoundingMode.HALF_UP);
            }

            TrackingQualityMetric metric = new TrackingQualityMetric();
            metric.setAppCode(bo.getAppCode());
            metric.setEventCode(bo.getEventCode());
            metric.setMetricTime(hourStart);
            metric.setMetricGranularity("HOUR");
            metric.setTotalCount(bo.getTotalCount());
            metric.setPassCount(bo.getPassCount());
            metric.setWarnCount(bo.getWarnCount());
            metric.setFailCount(bo.getFailCount());
            metric.setPassRate(passRate);

            trackingQualityMetricRepository.save(metric);
            count++;
        }

        log.info("[Job] 质量指标小时聚合完成，共写入 {} 条记录", count);
    }
}

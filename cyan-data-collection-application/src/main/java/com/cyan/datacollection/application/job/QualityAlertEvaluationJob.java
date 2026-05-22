package com.cyan.datacollection.application.job;

import com.cyan.datacollection.application.quality.bo.QualityOverviewBO;
import com.cyan.datacollection.domain.quality.TrackingAlert;
import com.cyan.datacollection.domain.quality.repository.TrackingAlertRepository;
import com.cyan.datacollection.domain.qualityrule.TrackingQualityRule;
import com.cyan.datacollection.domain.qualityrule.repository.TrackingQualityRuleRepository;
import com.cyan.datacollection.enums.AlertLevel;
import com.cyan.datacollection.enums.AlertType;
import com.cyan.datacollection.infra.persistence.collect.mappers.TrackingEventSampleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 质量告警评估定时任务
 * 每 10 分钟读取规则，扫描样本数据生成告警
 * 替换原 overview API 中的硬编码告警逻辑
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Component
public class QualityAlertEvaluationJob {

    private final TrackingQualityRuleRepository trackingQualityRuleRepository;
    private final TrackingAlertRepository trackingAlertRepository;
    private final TrackingEventSampleMapper trackingEventSampleMapper;

    public QualityAlertEvaluationJob(TrackingQualityRuleRepository trackingQualityRuleRepository,
                                     TrackingAlertRepository trackingAlertRepository,
                                     TrackingEventSampleMapper trackingEventSampleMapper) {
        this.trackingQualityRuleRepository = trackingQualityRuleRepository;
        this.trackingAlertRepository = trackingAlertRepository;
        this.trackingEventSampleMapper = trackingEventSampleMapper;
    }

    /**
     * 每 10 分钟执行一次
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void evaluateAlerts() {
        log.info("[Job] 开始执行质量告警评估");
        List<TrackingQualityRule> rules = trackingQualityRuleRepository.findEnabledRules();
        if (rules == null || rules.isEmpty()) {
            log.info("[Job] 无启用的质量规则，跳过评估");
            return;
        }

        int alertCount = 0;
        for (TrackingQualityRule rule : rules) {
            try {
                if (evaluateRule(rule)) {
                    alertCount++;
                }
            } catch (Exception e) {
                log.error("[Job] 规则评估异常, ruleCode={}", rule.getRuleCode(), e);
            }
        }
        log.info("[Job] 质量告警评估完成，生成 {} 条新告警", alertCount);
    }

    private boolean evaluateRule(TrackingQualityRule rule) {
        String appCode = rule.getAppCode();
        String eventCode = rule.getEventCode();
        int windowMinutes = rule.getTimeWindowMinutes() != null ? rule.getTimeWindowMinutes() : 60;
        LocalDateTime since = LocalDateTime.now().minusMinutes(windowMinutes);

        // 获取时间窗口内的聚合数据
        List<QualityOverviewBO> stats = trackingEventSampleMapper.aggregateByTimeRange(
                appCode, eventCode, since, LocalDateTime.now());

        if (stats == null || stats.isEmpty()) {
            return false;
        }

        boolean generated = false;
        for (QualityOverviewBO bo : stats) {
            if (AlertType.FAIL_RATE_HIGH.name().equals(rule.getAlertType())) {
                double threshold = rule.getThresholdValue() != null ? rule.getThresholdValue().doubleValue() : 0.2;
                if (bo.getTotalCount() != null && bo.getTotalCount() > 0) {
                    double failRate = (double) bo.getFailCount() / bo.getTotalCount();
                    if (failRate > threshold) {
                        generated = generateFailRateAlert(bo.getAppCode(), bo.getEventCode(), failRate, rule) || generated;
                    }
                }
            } else if (AlertType.NO_DATA.name().equals(rule.getAlertType())) {
                if (bo.getTotalCount() == null || bo.getTotalCount() == 0) {
                    generated = generateNoDataAlert(bo.getAppCode(), bo.getEventCode(), rule) || generated;
                }
            }
        }
        return generated;
    }

    private boolean generateFailRateAlert(String appCode, String eventCode, double failRate, TrackingQualityRule rule) {
        List<TrackingAlert> exists = trackingAlertRepository.findOpenByAppCodeAndEventCodeAndType(
                appCode, eventCode, AlertType.FAIL_RATE_HIGH);
        if (exists != null && !exists.isEmpty()) {
            return false;
        }
        TrackingAlert alert = new TrackingAlert();
        alert.setAlertType(AlertType.FAIL_RATE_HIGH);
        alert.setAppCode(appCode);
        alert.setEventCode(eventCode);
        alert.setAlertLevel(AlertLevel.of(rule.getAlertLevel()) != null ? AlertLevel.of(rule.getAlertLevel()) : AlertLevel.ERROR);
        alert.setAlertMessage(String.format("事件 %s 失败率 %.2f%%，超过阈值 %.2f%%", eventCode, failRate * 100,
                rule.getThresholdValue() != null ? rule.getThresholdValue().doubleValue() * 100 : 20));
        alert.save(trackingAlertRepository);
        return true;
    }

    private boolean generateNoDataAlert(String appCode, String eventCode, TrackingQualityRule rule) {
        List<TrackingAlert> exists = trackingAlertRepository.findOpenByAppCodeAndEventCodeAndType(
                appCode, eventCode, AlertType.NO_DATA);
        if (exists != null && !exists.isEmpty()) {
            return false;
        }
        TrackingAlert alert = new TrackingAlert();
        alert.setAlertType(AlertType.NO_DATA);
        alert.setAppCode(appCode);
        alert.setEventCode(eventCode);
        alert.setAlertLevel(AlertLevel.of(rule.getAlertLevel()) != null ? AlertLevel.of(rule.getAlertLevel()) : AlertLevel.WARN);
        alert.setAlertMessage(String.format("事件 %s 最近 %d 分钟无上报数据", eventCode,
                rule.getTimeWindowMinutes() != null ? rule.getTimeWindowMinutes() : 60));
        alert.save(trackingAlertRepository);
        return true;
    }
}

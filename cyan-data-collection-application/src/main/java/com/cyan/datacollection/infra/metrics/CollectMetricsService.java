package com.cyan.datacollection.infra.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 采集链路 Metrics 封装服务
 * 使用 Micrometer 暴露 HTTP 接收、Kafka 写入、Debug 样本等链路指标
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Component
public class CollectMetricsService {

    private final MeterRegistry meterRegistry;

    public CollectMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * 记录 HTTP 事件接收
     */
    public void recordHttpReceived(String appCode, String eventCode, String status) {
        Counter.builder("tracking.collect.http.received")
                .description("事件上报 HTTP 接收次数")
                .tags(Tags.of("app_code", appCode != null ? appCode : "unknown",
                        "event_code", eventCode != null ? eventCode : "unknown",
                        "status", status != null ? status : "unknown"))
                .register(meterRegistry)
                .increment();
    }

    /**
     * 记录 HTTP 事件接收失败
     */
    public void recordHttpFailed(String appCode, String eventCode, String reason) {
        Counter.builder("tracking.collect.http.failed")
                .description("事件上报 HTTP 接收失败次数")
                .tags(Tags.of("app_code", appCode != null ? appCode : "unknown",
                        "event_code", eventCode != null ? eventCode : "unknown",
                        "reason", reason != null ? reason : "unknown"))
                .register(meterRegistry)
                .increment();
    }

    /**
     * 记录 Kafka 发送成功
     */
    public void recordKafkaSent(String appCode, String eventCode) {
        Counter.builder("tracking.kafka.sent")
                .description("Kafka 事件发送成功次数")
                .tags(Tags.of("app_code", appCode != null ? appCode : "unknown",
                        "event_code", eventCode != null ? eventCode : "unknown"))
                .register(meterRegistry)
                .increment();
    }

    /**
     * 记录 Kafka 发送失败
     */
    public void recordKafkaFailed(String appCode, String eventCode, String reason) {
        Counter.builder("tracking.kafka.failed")
                .description("Kafka 事件发送失败次数")
                .tags(Tags.of("app_code", appCode != null ? appCode : "unknown",
                        "event_code", eventCode != null ? eventCode : "unknown",
                        "reason", reason != null ? reason : "unknown"))
                .register(meterRegistry)
                .increment();
    }

    /**
     * 记录 Debug 样本保存
     */
    public void recordDebugSampleSaved(String appCode, String eventCode, String validateStatus) {
        Counter.builder("tracking.debug.sample.saved")
                .description("Debug 样本保存次数")
                .tags(Tags.of("app_code", appCode != null ? appCode : "unknown",
                        "event_code", eventCode != null ? eventCode : "unknown",
                        "validate_status", validateStatus != null ? validateStatus : "unknown"))
                .register(meterRegistry)
                .increment();
    }

    /**
     * 记录事件处理耗时
     */
    public void recordCollectDuration(String appCode, String eventCode, long millis) {
        Timer.builder("tracking.collect.duration")
                .description("事件上报处理耗时(ms)")
                .tags(Tags.of("app_code", appCode != null ? appCode : "unknown",
                        "event_code", eventCode != null ? eventCode : "unknown"))
                .register(meterRegistry)
                .record(millis, TimeUnit.MILLISECONDS);
    }
}

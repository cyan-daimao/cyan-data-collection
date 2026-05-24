package com.cyan.datacollection.infra.kafka;

import com.alibaba.fastjson2.JSON;
import com.cyan.datacollection.infra.metrics.CollectMetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 事件上报 Kafka 生产者
 * 将原始事件发送到 Kafka Topic，供下游 Flink 消费入湖
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Component
public class TrackingEventKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CollectMetricsService collectMetricsService;

    @Value("${data-collection.kafka.topic:cyan_data_collection_event_raw}")
    private String topic;

    public TrackingEventKafkaProducer(KafkaTemplate<String, String> kafkaTemplate,
                                       CollectMetricsService collectMetricsService) {
        this.kafkaTemplate = kafkaTemplate;
        this.collectMetricsService = collectMetricsService;
    }

    /**
     * 发送事件到 Kafka
     *
     * @param message 事件消息 Map
     */
    public void send(Map<String, Object> message) {
        String key = (String) message.getOrDefault("request_id", "");
        String payload = JSON.toJSONString(message);

        String appCode = (String) message.getOrDefault("app_code", "unknown");
        String eventCode = (String) message.getOrDefault("event_code", "unknown");

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, payload);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("[Kafka] 事件发送失败, request_id={}, error={}", key, ex.getMessage(), ex);
                collectMetricsService.recordKafkaFailed(appCode, eventCode, ex.getClass().getSimpleName());
            } else {
                log.debug("[Kafka] 事件发送成功, request_id={}, partition={}, offset={}",
                        key, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                collectMetricsService.recordKafkaSent(appCode, eventCode);
            }
        });
    }

    /**
     * 同步发送事件到 Kafka，失败时抛出异常
     *
     * @param message 事件消息 Map
     */
    public void sendSync(Map<String, Object> message) throws Exception {
        String key = (String) message.getOrDefault("request_id", "");
        String payload = JSON.toJSONString(message);
        String appCode = (String) message.getOrDefault("app_code", "unknown");
        String eventCode = (String) message.getOrDefault("event_code", "unknown");

        try {
            SendResult<String, String> result = kafkaTemplate.send(topic, key, payload).get();
            log.debug("[Kafka] 事件同步发送成功, request_id={}, partition={}, offset={}",
                    key, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            collectMetricsService.recordKafkaSent(appCode, eventCode);
        } catch (Exception e) {
            collectMetricsService.recordKafkaFailed(appCode, eventCode, e.getClass().getSimpleName());
            throw e;
        }
    }
}

package com.cyan.datacollection.application.quality.impl;

import com.cyan.datacollection.application.quality.TrackingLinkStatusService;
import com.cyan.datacollection.application.quality.bo.LinkStatusBO;
import com.cyan.datacollection.infra.persistence.collect.mappers.TrackingEventSampleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 采集链路状态服务实现
 * 基于样本表聚合统计链路状态
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingLinkStatusServiceImpl implements TrackingLinkStatusService {

    private final TrackingEventSampleMapper trackingEventSampleMapper;

    public TrackingLinkStatusServiceImpl(TrackingEventSampleMapper trackingEventSampleMapper) {
        this.trackingEventSampleMapper = trackingEventSampleMapper;
    }

    @Override
    public LinkStatusBO getLinkStatus() {
        Long total = trackingEventSampleMapper.countToday();
        Long fail = trackingEventSampleMapper.countTodayByStatus("FAIL");
        Long debug = trackingEventSampleMapper.countTodayByDebugTokenNotNull();

        return new LinkStatusBO()
                .setHttpReceivedTotal(total)
                .setHttpFailedTotal(fail)
                .setKafkaSentTotal(total) // MVP 阶段：假设全部成功写入 Kafka
                .setKafkaFailedTotal(0L)  // 实际失败量需从 Metrics 暴露
                .setDebugSampleTotal(debug)
                .setStatTime(LocalDateTime.now());
    }
}

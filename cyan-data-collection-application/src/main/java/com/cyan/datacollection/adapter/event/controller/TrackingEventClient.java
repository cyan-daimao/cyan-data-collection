package com.cyan.datacollection.adapter.event.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.mapping.controller.dto.EventMetricMappingDTO;
import com.cyan.datacollection.adapter.mapping.controller.request.EventMetricSyncRequest;
import com.cyan.datacollection.adapter.event.controller.dto.EventPropertyDTO;
import com.cyan.datacollection.adapter.event.controller.dto.TrackingEventDTO;
import com.cyan.datacollection.adapter.event.controller.dto.TrackingEventUsageDTO;
import com.cyan.datacollection.adapter.event.controller.request.EventPropertyConfigRequest;
import com.cyan.datacollection.adapter.event.controller.request.TrackingEventCreateRequest;
import com.cyan.datacollection.adapter.event.controller.request.TrackingEventPageQuery;
import com.cyan.datacollection.adapter.event.controller.request.TrackingEventUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 事件定义 Feign 客户端
 *
 * @author cy.Y
 * @since 1.0.0
 */
@FeignClient(name = "cyan-data-collection", contextId = "trackingEventClient", path = "/api/data-collection/events", url = "${feign.cyan-data-collection.url:}")
public interface TrackingEventClient {

    /**
     * 分页查询事件
     */
    @PostMapping("/page")
    Response<PageResultDTO<TrackingEventDTO>> page(@RequestBody TrackingEventPageQuery query);

    /**
     * 创建事件
     */
    @PostMapping
    Response<TrackingEventDTO> create(@RequestBody @Valid TrackingEventCreateRequest request);

    /**
     * 更新事件
     */
    @PutMapping("/{id}")
    Response<TrackingEventDTO> update(@PathVariable("id") String id, @RequestBody @Valid TrackingEventUpdateRequest request);

    /**
     * 事件详情
     */
    @GetMapping("/{id}")
    Response<TrackingEventDTO> detail(@PathVariable("id") String id);

    /**
     * 发布事件
     */
    @PostMapping("/{id}/publish")
    Response<TrackingEventDTO> publish(@PathVariable("id") String id);

    /**
     * 废弃事件
     */
    @PostMapping("/{id}/deprecate")
    Response<TrackingEventDTO> deprecate(@PathVariable("id") String id);

    /**
     * 事件使用情况
     */
    @GetMapping("/{id}/usage")
    Response<TrackingEventUsageDTO> usage(@PathVariable("id") String id);

    /**
     * 配置事件属性
     */
    @PutMapping("/{id}/properties")
    Response<Void> configProperties(@PathVariable("id") String id, @RequestBody @Valid List<EventPropertyConfigRequest> requests);

    /**
     * 查询事件属性列表
     */
    @GetMapping("/{id}/properties")
    Response<List<EventPropertyDTO>> listProperties(@PathVariable("id") String id);

    /**
     * 事件同步为指标
     */
    @PostMapping("/{id}/sync-metric")
    Response<EventMetricMappingDTO> syncMetric(@PathVariable("id") String id, @RequestBody EventMetricSyncRequest request);

    /**
     * 查询事件指标映射
     */
    @GetMapping("/{id}/metric-mapping")
    Response<EventMetricMappingDTO> metricMapping(@PathVariable("id") String id);
}

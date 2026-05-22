package com.cyan.datacollection.adapter.acceptance.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.acceptance.controller.dto.TrackingAcceptanceTaskDTO;
import com.cyan.datacollection.adapter.acceptance.controller.request.TrackingAcceptanceTaskCreateRequest;
import com.cyan.datacollection.adapter.acceptance.controller.request.TrackingAcceptanceTaskPageQuery;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 验收任务 Feign 客户端
 *
 * @author cy.Y
 * @since 1.0.0
 */
@FeignClient(name = "cyan-data-collection", contextId = "trackingAcceptanceClient", path = "/api/data-collection/acceptance/tasks", url = "${feign.cyan-data-collection.url:}")
public interface TrackingAcceptanceClient {

    /**
     * 分页查询验收任务
     */
    @PostMapping("/page")
    Response<PageResultDTO<TrackingAcceptanceTaskDTO>> page(@RequestBody TrackingAcceptanceTaskPageQuery query);

    /**
     * 创建验收任务
     */
    @PostMapping
    Response<TrackingAcceptanceTaskDTO> create(@RequestBody TrackingAcceptanceTaskCreateRequest request);

    /**
     * 验收任务详情
     */
    @GetMapping("/{id}")
    Response<TrackingAcceptanceTaskDTO> detail(@PathVariable("id") String id);

    /**
     * 执行验收
     */
    @PostMapping("/{id}/run")
    Response<TrackingAcceptanceTaskDTO> run(@PathVariable("id") String id);

    /**
     * 验收通过
     */
    @PostMapping("/{id}/approve")
    Response<TrackingAcceptanceTaskDTO> approve(@PathVariable("id") String id);

    /**
     * 验收驳回
     */
    @PostMapping("/{id}/reject")
    Response<TrackingAcceptanceTaskDTO> reject(@PathVariable("id") String id);
}

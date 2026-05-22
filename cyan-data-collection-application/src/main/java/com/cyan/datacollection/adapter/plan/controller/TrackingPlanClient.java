package com.cyan.datacollection.adapter.plan.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.plan.controller.dto.TrackingPlanDTO;
import com.cyan.datacollection.adapter.plan.controller.dto.TrackingPlanEventDTO;
import com.cyan.datacollection.adapter.plan.controller.request.TrackingPlanCreateRequest;
import com.cyan.datacollection.adapter.plan.controller.request.TrackingPlanEventConfigRequest;
import com.cyan.datacollection.adapter.plan.controller.request.TrackingPlanPageQuery;
import com.cyan.datacollection.adapter.plan.controller.request.TrackingPlanUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 埋点方案 Feign 客户端
 *
 * @author cy.Y
 * @since 1.0.0
 */
@FeignClient(name = "cyan-data-collection", contextId = "trackingPlanClient", path = "/api/data-collection/plans", url = "${feign.cyan-data-collection.url:}")
public interface TrackingPlanClient {

    /**
     * 分页查询方案
     */
    @PostMapping("/page")
    Response<PageResultDTO<TrackingPlanDTO>> page(@RequestBody TrackingPlanPageQuery query);

    /**
     * 创建方案
     */
    @PostMapping
    Response<TrackingPlanDTO> create(@RequestBody TrackingPlanCreateRequest request);

    /**
     * 更新方案
     */
    @PutMapping("/{id}")
    Response<TrackingPlanDTO> update(@PathVariable("id") String id, @RequestBody TrackingPlanUpdateRequest request);

    /**
     * 方案详情
     */
    @GetMapping("/{id}")
    Response<TrackingPlanDTO> detail(@PathVariable("id") String id);

    /**
     * 添加事件到方案
     */
    @PostMapping("/{id}/events")
    Response<Void> addEvent(@PathVariable("id") String id, @RequestBody List<String> eventIds);

    /**
     * 移除事件
     */
    @DeleteMapping("/{id}/events/{eventId}")
    Response<Void> removeEvent(@PathVariable("id") String id, @PathVariable("eventId") String eventId);

    /**
     * 配置事件属性
     */
    @PutMapping("/{id}/events/{eventId}/properties")
    Response<Void> configEventProperties(@PathVariable("id") String id, @PathVariable("eventId") String eventId,
                                         @RequestBody List<TrackingPlanEventConfigRequest> requests);

    /**
     * 提交评审
     */
    @PostMapping("/{id}/submit-review")
    Response<TrackingPlanDTO> submitReview(@PathVariable("id") String id);

    /**
     * 评审通过
     */
    @PostMapping("/{id}/approve")
    Response<TrackingPlanDTO> approve(@PathVariable("id") String id);

    /**
     * 评审驳回
     */
    @PostMapping("/{id}/reject")
    Response<TrackingPlanDTO> reject(@PathVariable("id") String id);
}

package com.cyan.datacollection.adapter.debug.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.debug.controller.dto.DebugEventSampleDTO;
import com.cyan.datacollection.adapter.debug.controller.dto.DebugSessionDTO;
import com.cyan.datacollection.adapter.debug.controller.request.DebugEventPageQuery;
import com.cyan.datacollection.adapter.debug.controller.request.DebugSessionCreateRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Debug 控制台 Feign 客户端
 *
 * @author cy.Y
 * @since 1.0.0
 */
@FeignClient(name = "cyan-data-collection", contextId = "trackingDebugClient", path = "/api/data-collection/debug", url = "${feign.cyan-data-collection.url:}")
public interface TrackingDebugClient {

    /**
     * 创建 Debug 会话
     */
    @PostMapping("/sessions")
    Response<DebugSessionDTO> createSession(@RequestBody @Valid DebugSessionCreateRequest request);

    /**
     * 会话详情
     */
    @GetMapping("/sessions/{id}")
    Response<DebugSessionDTO> sessionDetail(@PathVariable("id") String id);

    /**
     * 查询 Debug 事件样本
     */
    @PostMapping("/events/page")
    Response<PageResultDTO<DebugEventSampleDTO>> eventPage(@RequestBody DebugEventPageQuery query);

    /**
     * 事件样本详情
     */
    @GetMapping("/events/{id}")
    Response<DebugEventSampleDTO> eventDetail(@PathVariable("id") String id);
}

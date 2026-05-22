package com.cyan.datacollection.adapter.collect.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.collect.controller.dto.CollectResultDTO;
import com.cyan.datacollection.adapter.collect.controller.request.EventCollectRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 事件上报 Feign 客户端
 *
 * @author cy.Y
 * @since 1.0.0
 */
@FeignClient(name = "cyan-data-collection", contextId = "trackingCollectClient", path = "/api/data-collection/collect", url = "${feign.cyan-data-collection.url:}")
public interface TrackingCollectClient {

    /**
     * 单条事件上报
     */
    @PostMapping("/events")
    Response<CollectResultDTO> collect(@RequestBody EventCollectRequest request);

    /**
     * 批量事件上报
     */
    @PostMapping("/events/batch")
    Response<List<CollectResultDTO>> collectBatch(@RequestBody List<EventCollectRequest> requests);
}

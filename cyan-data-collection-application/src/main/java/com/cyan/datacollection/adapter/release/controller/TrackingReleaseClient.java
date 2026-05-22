package com.cyan.datacollection.adapter.release.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.release.controller.dto.TrackingReleaseDTO;
import com.cyan.datacollection.adapter.release.controller.dto.TrackingReleaseItemDTO;
import com.cyan.datacollection.adapter.release.controller.request.TrackingReleaseCreateRequest;
import com.cyan.datacollection.adapter.release.controller.request.TrackingReleasePageQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 埋点发布版本 Feign 客户端
 *
 * @author cy.Y
 * @since 1.0.0
 */
@FeignClient(name = "cyan-data-collection", contextId = "trackingReleaseClient", path = "/api/data-collection/releases", url = "${feign.cyan-data-collection.url:}")
public interface TrackingReleaseClient {

    /**
     * 分页查询发布版本
     */
    @PostMapping("/page")
    Response<PageResultDTO<TrackingReleaseDTO>> page(@RequestBody TrackingReleasePageQuery query);

    /**
     * 创建发布版本
     */
    @PostMapping
    Response<TrackingReleaseDTO> create(@RequestBody TrackingReleaseCreateRequest request);

    /**
     * 发布详情
     */
    @GetMapping("/{id}")
    Response<TrackingReleaseDTO> detail(@PathVariable("id") String id);

    /**
     * 发布diff
     */
    @GetMapping("/{id}/diff")
    Response<List<TrackingReleaseItemDTO>> diff(@PathVariable("id") String id);

    /**
     * 提交发布
     */
    @PostMapping("/{id}/submit")
    Response<TrackingReleaseDTO> submit(@PathVariable("id") String id);

    /**
     * 发布上线
     */
    @PostMapping("/{id}/publish")
    Response<TrackingReleaseDTO> publish(@PathVariable("id") String id);
}

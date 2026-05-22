package com.cyan.datacollection.adapter.property.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.property.controller.dto.TrackingPropertyDTO;
import com.cyan.datacollection.adapter.property.controller.dto.TrackingPropertyUsageDTO;
import com.cyan.datacollection.adapter.property.controller.request.TrackingPropertyCreateRequest;
import com.cyan.datacollection.adapter.property.controller.request.TrackingPropertyPageQuery;
import com.cyan.datacollection.adapter.property.controller.request.TrackingPropertyUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 属性定义 Feign 客户端
 *
 * @author cy.Y
 * @since 1.0.0
 */
@FeignClient(name = "cyan-data-collection", contextId = "trackingPropertyClient", path = "/api/data-collection/properties", url = "${feign.cyan-data-collection.url:}")
public interface TrackingPropertyClient {

    /**
     * 分页查询属性
     */
    @PostMapping("/page")
    Response<PageResultDTO<TrackingPropertyDTO>> page(@RequestBody TrackingPropertyPageQuery query);

    /**
     * 创建属性
     */
    @PostMapping
    Response<TrackingPropertyDTO> create(@RequestBody TrackingPropertyCreateRequest request);

    /**
     * 更新属性
     */
    @PutMapping("/{id}")
    Response<TrackingPropertyDTO> update(@PathVariable("id") String id, @RequestBody TrackingPropertyUpdateRequest request);

    /**
     * 属性详情
     */
    @GetMapping("/{id}")
    Response<TrackingPropertyDTO> detail(@PathVariable("id") String id);

    /**
     * 发布属性
     */
    @PostMapping("/{id}/publish")
    Response<TrackingPropertyDTO> publish(@PathVariable("id") String id);

    /**
     * 废弃属性
     */
    @PostMapping("/{id}/deprecate")
    Response<TrackingPropertyDTO> deprecate(@PathVariable("id") String id);

    /**
     * 属性使用情况
     */
    @GetMapping("/{id}/usage")
    Response<TrackingPropertyUsageDTO> usage(@PathVariable("id") String id);
}

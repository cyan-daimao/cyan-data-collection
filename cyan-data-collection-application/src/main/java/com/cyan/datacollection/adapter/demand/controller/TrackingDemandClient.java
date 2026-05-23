package com.cyan.datacollection.adapter.demand.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.demand.controller.dto.TrackingDemandDTO;
import com.cyan.datacollection.adapter.demand.controller.request.TrackingDemandCreateRequest;
import com.cyan.datacollection.adapter.demand.controller.request.TrackingDemandPageQuery;
import com.cyan.datacollection.adapter.demand.controller.request.TrackingDemandUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 埋点需求 Feign 客户端
 *
 * @author cy.Y
 * @since 1.0.0
 */
@FeignClient(name = "cyan-data-collection", contextId = "trackingDemandClient", path = "/api/data-collection/demands", url = "${feign.cyan-data-collection.url:}")
public interface TrackingDemandClient {

    /**
     * 分页查询需求
     */
    @PostMapping("/page")
    Response<PageResultDTO<TrackingDemandDTO>> page(@RequestBody TrackingDemandPageQuery query);

    /**
     * 创建需求
     */
    @PostMapping
    Response<TrackingDemandDTO> create(@RequestBody @Valid TrackingDemandCreateRequest request);

    /**
     * 更新需求
     */
    @PutMapping("/{id}")
    Response<TrackingDemandDTO> update(@PathVariable("id") String id, @RequestBody @Valid TrackingDemandUpdateRequest request);

    /**
     * 需求详情
     */
    @GetMapping("/{id}")
    Response<TrackingDemandDTO> detail(@PathVariable("id") String id);

    /**
     * 提交设计
     */
    @PostMapping("/{id}/submit-design")
    Response<TrackingDemandDTO> submitDesign(@PathVariable("id") String id);

    /**
     * 关闭需求
     */
    @PostMapping("/{id}/close")
    Response<TrackingDemandDTO> close(@PathVariable("id") String id);
}

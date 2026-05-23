package com.cyan.datacollection.adapter.app.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.app.controller.dto.TrackingAppDTO;
import com.cyan.datacollection.adapter.app.controller.dto.TrackingAppIntegrationDTO;
import com.cyan.datacollection.adapter.app.controller.request.TrackingAppCreateRequest;
import com.cyan.datacollection.adapter.app.controller.request.TrackingAppPageQuery;
import com.cyan.datacollection.adapter.app.controller.request.TrackingAppUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 接入应用 Feign 客户端
 *
 * @author cy.Y
 * @since 1.0.0
 */
@FeignClient(name = "cyan-data-collection", contextId = "trackingAppClient", path = "/api/data-collection/apps", url = "${feign.cyan-data-collection.url:}")
public interface TrackingAppClient {

    /**
     * 分页查询应用
     */
    @PostMapping("/page")
    Response<PageResultDTO<TrackingAppDTO>> page(@RequestBody TrackingAppPageQuery query);

    /**
     * 创建应用
     */
    @PostMapping
    Response<TrackingAppDTO> create(@RequestBody @Valid TrackingAppCreateRequest request);

    /**
     * 更新应用
     */
    @PutMapping("/{id}")
    Response<TrackingAppDTO> update(@PathVariable("id") String id, @RequestBody @Valid TrackingAppUpdateRequest request);

    /**
     * 应用详情
     */
    @GetMapping("/{id}")
    Response<TrackingAppDTO> detail(@PathVariable("id") String id);

    /**
     * 轮换密钥
     */
    @PostMapping("/{id}/secret/rotate")
    Response<TrackingAppDTO> rotateSecret(@PathVariable("id") String id);

    /**
     * 获取接入示例
     */
    @GetMapping("/{id}/integration-code")
    Response<TrackingAppIntegrationDTO> integrationCode(@PathVariable("id") String id);
}

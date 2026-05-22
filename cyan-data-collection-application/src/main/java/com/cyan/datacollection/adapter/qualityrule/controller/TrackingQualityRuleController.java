package com.cyan.datacollection.adapter.qualityrule.controller;

import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.qualityrule.controller.convert.TrackingQualityRuleAdapterConvert;
import com.cyan.datacollection.adapter.qualityrule.controller.dto.TrackingQualityRuleDTO;
import com.cyan.datacollection.adapter.qualityrule.controller.request.TrackingQualityRulePageRequest;
import com.cyan.datacollection.adapter.qualityrule.controller.request.TrackingQualityRuleRequest;
import com.cyan.datacollection.application.qualityrule.TrackingQualityRuleService;
import com.cyan.datacollection.application.qualityrule.bo.TrackingQualityRuleBO;
import com.cyan.datacollection.domain.qualityrule.query.TrackingQualityRulePageQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 质量规则配置控制器
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/quality/rules")
public class TrackingQualityRuleController {

    private final TrackingQualityRuleService trackingQualityRuleService;

    public TrackingQualityRuleController(TrackingQualityRuleService trackingQualityRuleService) {
        this.trackingQualityRuleService = trackingQualityRuleService;
    }

    @PostMapping
    public Response<TrackingQualityRuleDTO> create(@RequestBody TrackingQualityRuleRequest request) {
        TrackingQualityRuleBO bo = trackingQualityRuleService.create(
                TrackingQualityRuleAdapterConvert.INSTANCE.toCmd(request));
        return Response.success(TrackingQualityRuleAdapterConvert.INSTANCE.toDTO(bo));
    }

    @PutMapping("/{id}")
    public Response<TrackingQualityRuleDTO> update(@PathVariable String id,
                                                   @RequestBody TrackingQualityRuleRequest request) {
        TrackingQualityRuleBO bo = trackingQualityRuleService.update(id,
                TrackingQualityRuleAdapterConvert.INSTANCE.toCmd(request));
        return Response.success(TrackingQualityRuleAdapterConvert.INSTANCE.toDTO(bo));
    }

    @GetMapping("/{id}")
    public Response<TrackingQualityRuleDTO> getById(@PathVariable String id) {
        TrackingQualityRuleBO bo = trackingQualityRuleService.getById(id);
        return Response.success(TrackingQualityRuleAdapterConvert.INSTANCE.toDTO(bo));
    }

    @PostMapping("/page")
    public Response<PageResultDTO<TrackingQualityRuleDTO>> page(@RequestBody TrackingQualityRulePageRequest request) {
        TrackingQualityRulePageQuery query = TrackingQualityRuleAdapterConvert.INSTANCE.toPageQuery(request);
        Page<TrackingQualityRuleBO> page = trackingQualityRuleService.page(query);
        return Response.success(new PageResultDTO<>(
                page.getData().stream().map(TrackingQualityRuleAdapterConvert.INSTANCE::toDTO).toList(),
                page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @PostMapping("/{id}/enable")
    public Response<TrackingQualityRuleDTO> enable(@PathVariable String id) {
        TrackingQualityRuleBO bo = trackingQualityRuleService.enable(id);
        return Response.success(TrackingQualityRuleAdapterConvert.INSTANCE.toDTO(bo));
    }

    @PostMapping("/{id}/disable")
    public Response<TrackingQualityRuleDTO> disable(@PathVariable String id) {
        TrackingQualityRuleBO bo = trackingQualityRuleService.disable(id);
        return Response.success(TrackingQualityRuleAdapterConvert.INSTANCE.toDTO(bo));
    }

    @GetMapping("/enabled")
    public Response<List<TrackingQualityRuleDTO>> listEnabled() {
        List<TrackingQualityRuleBO> bos = trackingQualityRuleService.listEnabledRules();
        return Response.success(bos.stream()
                .map(TrackingQualityRuleAdapterConvert.INSTANCE::toDTO)
                .toList());
    }
}

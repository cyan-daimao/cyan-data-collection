package com.cyan.datacollection.adapter.property.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.mapping.controller.convert.TrackingMappingAdapterConvert;
import com.cyan.datacollection.adapter.mapping.controller.dto.PropertyDimensionMappingDTO;
import com.cyan.datacollection.adapter.mapping.controller.request.PropertyDimensionSyncRequest;
import com.cyan.datacollection.adapter.property.controller.convert.TrackingPropertyAdapterConvert;
import com.cyan.datacollection.application.mapping.TrackingMetricMappingService;
import com.cyan.datacollection.application.mapping.bo.PropertyDimensionMappingBO;
import com.cyan.datacollection.application.property.TrackingPropertyService;
import com.cyan.datacollection.application.property.bo.TrackingPropertyBO;
import com.cyan.datacollection.application.property.cmd.TrackingPropertyCmd;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.property.controller.TrackingPropertyClient;
import com.cyan.datacollection.adapter.property.controller.dto.TrackingPropertyDTO;
import com.cyan.datacollection.adapter.property.controller.dto.TrackingPropertyUsageDTO;
import com.cyan.datacollection.adapter.property.controller.request.TrackingPropertyCreateRequest;
import com.cyan.datacollection.adapter.property.controller.request.TrackingPropertyPageQuery;
import com.cyan.datacollection.adapter.property.controller.request.TrackingPropertyUpdateRequest;
import com.cyan.employee.login.filter.UserContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 属性定义控制器
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/properties")
public class TrackingPropertyController implements TrackingPropertyClient {

    private final TrackingPropertyService trackingPropertyService;
    private final TrackingMetricMappingService trackingMetricMappingService;

    public TrackingPropertyController(TrackingPropertyService trackingPropertyService,
                                      TrackingMetricMappingService trackingMetricMappingService) {
        this.trackingPropertyService = trackingPropertyService;
        this.trackingMetricMappingService = trackingMetricMappingService;
    }

    @Override
    @PostMapping("/page")
    public Response<PageResultDTO<TrackingPropertyDTO>> page(@RequestBody TrackingPropertyPageQuery query) {
        var page = trackingPropertyService.page(TrackingPropertyAdapterConvert.INSTANCE.toPageQuery(query));
        return Response.success(new PageResultDTO<>(
                page.getData().stream().map(TrackingPropertyAdapterConvert.INSTANCE::toClientDTO).toList(),
                page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @Override
    @PostMapping
    public Response<TrackingPropertyDTO> create(@RequestBody @Valid TrackingPropertyCreateRequest request) {
        TrackingPropertyCmd cmd = TrackingPropertyAdapterConvert.INSTANCE.toCmd(request);
        cmd.setCreateBy(UserContextHolder.getCurrentEmployee().getPassport());
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingPropertyBO bo = trackingPropertyService.create(cmd);
        return Response.success(TrackingPropertyAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PutMapping("/{id}")
    public Response<TrackingPropertyDTO> update(@PathVariable("id") String id, @RequestBody @Valid TrackingPropertyUpdateRequest request) {
        TrackingPropertyCmd cmd = TrackingPropertyAdapterConvert.INSTANCE.toCmd(request);
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingPropertyBO bo = trackingPropertyService.update(id, cmd);
        return Response.success(TrackingPropertyAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @GetMapping("/{id}")
    public Response<TrackingPropertyDTO> detail(@PathVariable("id") String id) {
        TrackingPropertyBO bo = trackingPropertyService.detail(id);
        return Response.success(TrackingPropertyAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/publish")
    public Response<TrackingPropertyDTO> publish(@PathVariable("id") String id) {
        TrackingPropertyBO bo = trackingPropertyService.publish(id);
        return Response.success(TrackingPropertyAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/deprecate")
    public Response<TrackingPropertyDTO> deprecate(@PathVariable("id") String id) {
        TrackingPropertyBO bo = trackingPropertyService.deprecate(id);
        return Response.success(TrackingPropertyAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @GetMapping("/{id}/usage")
    public Response<TrackingPropertyUsageDTO> usage(@PathVariable("id") String id) {
        TrackingPropertyBO.UsageBO usage = trackingPropertyService.usage(id);
        return Response.success(TrackingPropertyAdapterConvert.INSTANCE.toClientUsageDTO(usage));
    }

    @Override
    @PostMapping("/{id}/sync-dimension")
    public Response<PropertyDimensionMappingDTO> syncDimension(@PathVariable("id") String id,
                                                              @RequestBody PropertyDimensionSyncRequest request) {
        var cmd = TrackingMappingAdapterConvert.INSTANCE.toCmd(request == null ? new PropertyDimensionSyncRequest() : request);
        cmd.setOperator(UserContextHolder.getCurrentEmployee().getPassport());
        PropertyDimensionMappingBO bo = trackingMetricMappingService.syncPropertyDimension(id, cmd);
        return Response.success(TrackingMappingAdapterConvert.INSTANCE.toDTO(bo));
    }

    @Override
    @GetMapping("/{id}/dimension-mapping")
    public Response<PropertyDimensionMappingDTO> dimensionMapping(@PathVariable("id") String id) {
        PropertyDimensionMappingBO bo = trackingMetricMappingService.getPropertyDimensionMapping(id);
        return Response.success(TrackingMappingAdapterConvert.INSTANCE.toDTO(bo));
    }
}

package com.cyan.datacollection.adapter.event.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.event.controller.convert.TrackingEventAdapterConvert;
import com.cyan.datacollection.application.event.TrackingEventService;
import com.cyan.datacollection.application.event.bo.TrackingEventBO;
import com.cyan.datacollection.application.event.cmd.TrackingEventCmd;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.event.controller.TrackingEventClient;
import com.cyan.datacollection.adapter.event.controller.dto.TrackingEventDTO;
import com.cyan.datacollection.adapter.event.controller.dto.TrackingEventUsageDTO;
import com.cyan.datacollection.adapter.event.controller.request.TrackingEventCreateRequest;
import com.cyan.datacollection.adapter.event.controller.request.TrackingEventPageQuery;
import com.cyan.datacollection.adapter.event.controller.request.TrackingEventUpdateRequest;
import com.cyan.employee.login.filter.UserContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 事件定义控制器
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/events")
public class TrackingEventController implements TrackingEventClient {

    private final TrackingEventService trackingEventService;

    public TrackingEventController(TrackingEventService trackingEventService) {
        this.trackingEventService = trackingEventService;
    }

    @Override
    @PostMapping("/page")
    public Response<PageResultDTO<TrackingEventDTO>> page(@RequestBody TrackingEventPageQuery query) {
        var page = trackingEventService.page(TrackingEventAdapterConvert.INSTANCE.toPageQuery(query));
        return Response.success(new PageResultDTO<>(
                page.getData().stream().map(TrackingEventAdapterConvert.INSTANCE::toClientDTO).toList(),
                page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @Override
    @PostMapping
    public Response<TrackingEventDTO> create(@RequestBody @Valid TrackingEventCreateRequest request) {
        TrackingEventCmd cmd = TrackingEventAdapterConvert.INSTANCE.toCmd(request);
        cmd.setCreateBy(UserContextHolder.getCurrentEmployee().getPassport());
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingEventBO bo = trackingEventService.create(cmd);
        return Response.success(TrackingEventAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PutMapping("/{id}")
    public Response<TrackingEventDTO> update(@PathVariable("id") String id, @RequestBody @Valid TrackingEventUpdateRequest request) {
        TrackingEventCmd cmd = TrackingEventAdapterConvert.INSTANCE.toCmd(request);
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingEventBO bo = trackingEventService.update(id, cmd);
        return Response.success(TrackingEventAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @GetMapping("/{id}")
    public Response<TrackingEventDTO> detail(@PathVariable("id") String id) {
        TrackingEventBO bo = trackingEventService.detail(id);
        return Response.success(TrackingEventAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/publish")
    public Response<TrackingEventDTO> publish(@PathVariable("id") String id) {
        TrackingEventBO bo = trackingEventService.publish(id);
        return Response.success(TrackingEventAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/deprecate")
    public Response<TrackingEventDTO> deprecate(@PathVariable("id") String id) {
        TrackingEventBO bo = trackingEventService.deprecate(id);
        return Response.success(TrackingEventAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @GetMapping("/{id}/usage")
    public Response<TrackingEventUsageDTO> usage(@PathVariable("id") String id) {
        TrackingEventBO.UsageBO usage = trackingEventService.usage(id);
        return Response.success(TrackingEventAdapterConvert.INSTANCE.toClientUsageDTO(usage));
    }
}

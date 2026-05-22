package com.cyan.datacollection.adapter.acceptance.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.acceptance.controller.convert.TrackingAcceptanceAdapterConvert;
import com.cyan.datacollection.adapter.acceptance.controller.dto.TrackingAcceptanceTaskDTO;
import com.cyan.datacollection.adapter.acceptance.controller.request.TrackingAcceptanceTaskCreateRequest;
import com.cyan.datacollection.adapter.acceptance.controller.request.TrackingAcceptanceTaskPageQuery;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.application.acceptance.TrackingAcceptanceService;
import com.cyan.datacollection.application.acceptance.bo.TrackingAcceptanceTaskBO;
import com.cyan.datacollection.application.acceptance.cmd.TrackingAcceptanceTaskCmd;
import com.cyan.employee.login.filter.UserContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验收任务控制器
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/acceptance/tasks")
public class TrackingAcceptanceController implements TrackingAcceptanceClient {

    private final TrackingAcceptanceService trackingAcceptanceService;

    public TrackingAcceptanceController(TrackingAcceptanceService trackingAcceptanceService) {
        this.trackingAcceptanceService = trackingAcceptanceService;
    }

    @Override
    @PostMapping("/page")
    public Response<PageResultDTO<TrackingAcceptanceTaskDTO>> page(@RequestBody TrackingAcceptanceTaskPageQuery query) {
        var page = trackingAcceptanceService.page(TrackingAcceptanceAdapterConvert.INSTANCE.toPageQuery(query));
        return Response.success(new PageResultDTO<>(
                page.getData().stream().map(TrackingAcceptanceAdapterConvert.INSTANCE::toClientDTO).toList(),
                page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @Override
    @PostMapping
    public Response<TrackingAcceptanceTaskDTO> create(@RequestBody @Valid TrackingAcceptanceTaskCreateRequest request) {
        TrackingAcceptanceTaskCmd cmd = TrackingAcceptanceAdapterConvert.INSTANCE.toCmd(request);
        cmd.setCreateBy(UserContextHolder.getCurrentEmployee().getPassport());
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingAcceptanceTaskBO bo = trackingAcceptanceService.create(cmd);
        return Response.success(TrackingAcceptanceAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @GetMapping("/{id}")
    public Response<TrackingAcceptanceTaskDTO> detail(@PathVariable("id") String id) {
        TrackingAcceptanceTaskBO bo = trackingAcceptanceService.detail(id);
        return Response.success(TrackingAcceptanceAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/run")
    public Response<TrackingAcceptanceTaskDTO> run(@PathVariable("id") String id) {
        TrackingAcceptanceTaskBO bo = trackingAcceptanceService.run(id);
        return Response.success(TrackingAcceptanceAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/approve")
    public Response<TrackingAcceptanceTaskDTO> approve(@PathVariable("id") String id) {
        TrackingAcceptanceTaskBO bo = trackingAcceptanceService.approve(id);
        return Response.success(TrackingAcceptanceAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/reject")
    public Response<TrackingAcceptanceTaskDTO> reject(@PathVariable("id") String id) {
        TrackingAcceptanceTaskBO bo = trackingAcceptanceService.reject(id);
        return Response.success(TrackingAcceptanceAdapterConvert.INSTANCE.toClientDTO(bo));
    }
}

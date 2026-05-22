package com.cyan.datacollection.adapter.demand.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.demand.controller.convert.TrackingDemandAdapterConvert;
import com.cyan.datacollection.application.demand.TrackingDemandService;
import com.cyan.datacollection.application.demand.bo.TrackingDemandBO;
import com.cyan.datacollection.application.demand.cmd.TrackingDemandCmd;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.demand.controller.TrackingDemandClient;
import com.cyan.datacollection.adapter.demand.controller.dto.TrackingDemandDTO;
import com.cyan.datacollection.adapter.demand.controller.request.TrackingDemandCreateRequest;
import com.cyan.datacollection.adapter.demand.controller.request.TrackingDemandPageQuery;
import com.cyan.datacollection.adapter.demand.controller.request.TrackingDemandUpdateRequest;
import com.cyan.employee.login.filter.UserContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 埋点需求控制器
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/demands")
public class TrackingDemandController implements TrackingDemandClient {

    private final TrackingDemandService trackingDemandService;

    public TrackingDemandController(TrackingDemandService trackingDemandService) {
        this.trackingDemandService = trackingDemandService;
    }

    @Override
    @PostMapping("/page")
    public Response<PageResultDTO<TrackingDemandDTO>> page(@RequestBody TrackingDemandPageQuery query) {
        var page = trackingDemandService.page(TrackingDemandAdapterConvert.INSTANCE.toPageQuery(query));
        return Response.success(new PageResultDTO<>(
                page.getData().stream().map(TrackingDemandAdapterConvert.INSTANCE::toClientDTO).toList(),
                page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @Override
    @PostMapping
    public Response<TrackingDemandDTO> create(@RequestBody @Valid TrackingDemandCreateRequest request) {
        TrackingDemandCmd cmd = TrackingDemandAdapterConvert.INSTANCE.toCmd(request);
        cmd.setCreateBy(UserContextHolder.getCurrentEmployee().getPassport());
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingDemandBO bo = trackingDemandService.create(cmd);
        return Response.success(TrackingDemandAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PutMapping("/{id}")
    public Response<TrackingDemandDTO> update(@PathVariable("id") String id, @RequestBody @Valid TrackingDemandUpdateRequest request) {
        TrackingDemandCmd cmd = TrackingDemandAdapterConvert.INSTANCE.toCmd(request);
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingDemandBO bo = trackingDemandService.update(id, cmd);
        return Response.success(TrackingDemandAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @GetMapping("/{id}")
    public Response<TrackingDemandDTO> detail(@PathVariable("id") String id) {
        TrackingDemandBO bo = trackingDemandService.detail(id);
        return Response.success(TrackingDemandAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/submit-design")
    public Response<TrackingDemandDTO> submitDesign(@PathVariable("id") String id) {
        TrackingDemandBO bo = trackingDemandService.submitDesign(id);
        return Response.success(TrackingDemandAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/close")
    public Response<TrackingDemandDTO> close(@PathVariable("id") String id) {
        TrackingDemandBO bo = trackingDemandService.close(id);
        return Response.success(TrackingDemandAdapterConvert.INSTANCE.toClientDTO(bo));
    }
}

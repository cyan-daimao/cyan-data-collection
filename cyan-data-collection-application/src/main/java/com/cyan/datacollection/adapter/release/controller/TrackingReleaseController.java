package com.cyan.datacollection.adapter.release.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.release.controller.convert.TrackingReleaseAdapterConvert;
import com.cyan.datacollection.adapter.release.controller.dto.TrackingReleaseDTO;
import com.cyan.datacollection.adapter.release.controller.dto.TrackingReleaseItemDTO;
import com.cyan.datacollection.adapter.release.controller.request.TrackingReleaseCreateRequest;
import com.cyan.datacollection.adapter.release.controller.request.TrackingReleasePageQuery;
import com.cyan.datacollection.application.release.TrackingReleaseService;
import com.cyan.datacollection.application.release.bo.TrackingReleaseBO;
import com.cyan.datacollection.application.release.cmd.TrackingReleaseCmd;
import com.cyan.employee.login.filter.UserContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 埋点发布版本控制器
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/releases")
public class TrackingReleaseController implements TrackingReleaseClient {

    private final TrackingReleaseService trackingReleaseService;

    public TrackingReleaseController(TrackingReleaseService trackingReleaseService) {
        this.trackingReleaseService = trackingReleaseService;
    }

    @Override
    @PostMapping("/page")
    public Response<PageResultDTO<TrackingReleaseDTO>> page(@RequestBody TrackingReleasePageQuery query) {
        var page = trackingReleaseService.page(TrackingReleaseAdapterConvert.INSTANCE.toPageQuery(query));
        return Response.success(new PageResultDTO<>(
                page.getData().stream().map(TrackingReleaseAdapterConvert.INSTANCE::toClientDTO).toList(),
                page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @Override
    @PostMapping
    public Response<TrackingReleaseDTO> create(@RequestBody @Valid TrackingReleaseCreateRequest request) {
        TrackingReleaseCmd cmd = TrackingReleaseAdapterConvert.INSTANCE.toCmd(request);
        cmd.setCreateBy(UserContextHolder.getCurrentEmployee().getPassport());
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingReleaseBO bo = trackingReleaseService.create(cmd);
        return Response.success(TrackingReleaseAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @GetMapping("/{id}")
    public Response<TrackingReleaseDTO> detail(@PathVariable("id") String id) {
        TrackingReleaseBO bo = trackingReleaseService.detail(id);
        return Response.success(TrackingReleaseAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @GetMapping("/{id}/diff")
    public Response<List<TrackingReleaseItemDTO>> diff(@PathVariable("id") String id) {
        List<TrackingReleaseBO.ItemBO> items = trackingReleaseService.diff(id);
        return Response.success(items.stream()
                .map(TrackingReleaseAdapterConvert.INSTANCE::toItemDTO)
                .toList());
    }

    @Override
    @PostMapping("/{id}/submit")
    public Response<TrackingReleaseDTO> submit(@PathVariable("id") String id) {
        TrackingReleaseBO bo = trackingReleaseService.submit(id);
        return Response.success(TrackingReleaseAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/publish")
    public Response<TrackingReleaseDTO> publish(@PathVariable("id") String id) {
        TrackingReleaseBO bo = trackingReleaseService.publish(id);
        return Response.success(TrackingReleaseAdapterConvert.INSTANCE.toClientDTO(bo));
    }
}

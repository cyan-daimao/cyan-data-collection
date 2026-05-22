package com.cyan.datacollection.adapter.plan.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.plan.controller.convert.TrackingPlanAdapterConvert;
import com.cyan.datacollection.application.plan.TrackingPlanService;
import com.cyan.datacollection.application.plan.bo.TrackingPlanBO;
import com.cyan.datacollection.application.plan.cmd.TrackingPlanCmd;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.plan.controller.TrackingPlanClient;
import com.cyan.datacollection.adapter.plan.controller.dto.TrackingPlanDTO;
import com.cyan.datacollection.adapter.plan.controller.request.TrackingPlanCreateRequest;
import com.cyan.datacollection.adapter.plan.controller.request.TrackingPlanEventConfigRequest;
import com.cyan.datacollection.adapter.plan.controller.request.TrackingPlanPageQuery;
import com.cyan.datacollection.adapter.plan.controller.request.TrackingPlanUpdateRequest;
import com.cyan.employee.login.filter.UserContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 埋点方案控制器
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/plans")
public class TrackingPlanController implements TrackingPlanClient {

    private final TrackingPlanService trackingPlanService;

    public TrackingPlanController(TrackingPlanService trackingPlanService) {
        this.trackingPlanService = trackingPlanService;
    }

    @Override
    @PostMapping("/page")
    public Response<PageResultDTO<TrackingPlanDTO>> page(@RequestBody TrackingPlanPageQuery query) {
        var page = trackingPlanService.page(TrackingPlanAdapterConvert.INSTANCE.toPageQuery(query));
        return Response.success(new PageResultDTO<>(
                page.getData().stream().map(TrackingPlanAdapterConvert.INSTANCE::toClientDTO).toList(),
                page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @Override
    @PostMapping
    public Response<TrackingPlanDTO> create(@RequestBody @Valid TrackingPlanCreateRequest request) {
        TrackingPlanCmd cmd = TrackingPlanAdapterConvert.INSTANCE.toCmd(request);
        cmd.setCreateBy(UserContextHolder.getCurrentEmployee().getPassport());
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingPlanBO bo = trackingPlanService.create(cmd);
        return Response.success(TrackingPlanAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PutMapping("/{id}")
    public Response<TrackingPlanDTO> update(@PathVariable("id") String id, @RequestBody @Valid TrackingPlanUpdateRequest request) {
        TrackingPlanCmd cmd = TrackingPlanAdapterConvert.INSTANCE.toCmd(request);
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingPlanBO bo = trackingPlanService.update(id, cmd);
        return Response.success(TrackingPlanAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @GetMapping("/{id}")
    public Response<TrackingPlanDTO> detail(@PathVariable("id") String id) {
        TrackingPlanBO bo = trackingPlanService.detail(id);
        return Response.success(TrackingPlanAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/events")
    public Response<Void> addEvent(@PathVariable("id") String id, @RequestBody List<String> eventIds) {
        trackingPlanService.addEvent(id, eventIds);
        return Response.success();
    }

    @Override
    @DeleteMapping("/{id}/events/{eventId}")
    public Response<Void> removeEvent(@PathVariable("id") String id, @PathVariable("eventId") String eventId) {
        trackingPlanService.removeEvent(id, eventId);
        return Response.success();
    }

    @Override
    @PutMapping("/{id}/events/{eventId}/properties")
    public Response<Void> configEventProperties(@PathVariable("id") String id, @PathVariable("eventId") String eventId,
                                                @RequestBody List<TrackingPlanEventConfigRequest> requests) {
        var cmds = requests.stream()
                .map(TrackingPlanAdapterConvert.INSTANCE::toCmd)
                .toList();
        trackingPlanService.configEventProperties(id, eventId, cmds);
        return Response.success();
    }

    @Override
    @PostMapping("/{id}/submit-review")
    public Response<TrackingPlanDTO> submitReview(@PathVariable("id") String id) {
        TrackingPlanBO bo = trackingPlanService.submitReview(id);
        return Response.success(TrackingPlanAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/approve")
    public Response<TrackingPlanDTO> approve(@PathVariable("id") String id) {
        TrackingPlanBO bo = trackingPlanService.approve(id);
        return Response.success(TrackingPlanAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/reject")
    public Response<TrackingPlanDTO> reject(@PathVariable("id") String id) {
        TrackingPlanBO bo = trackingPlanService.reject(id);
        return Response.success(TrackingPlanAdapterConvert.INSTANCE.toClientDTO(bo));
    }
}

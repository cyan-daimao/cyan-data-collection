package com.cyan.datacollection.adapter.debug.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.debug.controller.convert.TrackingDebugAdapterConvert;
import com.cyan.datacollection.application.debug.TrackingDebugService;
import com.cyan.datacollection.application.debug.bo.DebugEventSampleBO;
import com.cyan.datacollection.application.debug.bo.DebugSessionBO;
import com.cyan.datacollection.application.debug.cmd.DebugSessionCmd;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.debug.controller.TrackingDebugClient;
import com.cyan.datacollection.adapter.debug.controller.dto.DebugEventSampleDTO;
import com.cyan.datacollection.adapter.debug.controller.dto.DebugSessionDTO;
import com.cyan.datacollection.adapter.debug.controller.request.DebugEventPageQuery;
import com.cyan.datacollection.adapter.debug.controller.request.DebugSessionCreateRequest;
import com.cyan.employee.login.filter.UserContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * Debug 控制台控制器
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/debug")
public class TrackingDebugController implements TrackingDebugClient {

    private final TrackingDebugService trackingDebugService;

    public TrackingDebugController(TrackingDebugService trackingDebugService) {
        this.trackingDebugService = trackingDebugService;
    }

    @Override
    @PostMapping("/sessions")
    public Response<DebugSessionDTO> createSession(@RequestBody @Valid DebugSessionCreateRequest request) {
        DebugSessionCmd cmd = TrackingDebugAdapterConvert.INSTANCE.toCmd(request);
        cmd.setCreateBy(UserContextHolder.getCurrentEmployee().getPassport());
        DebugSessionBO bo = trackingDebugService.createSession(cmd);
        return Response.success(TrackingDebugAdapterConvert.INSTANCE.toClientSessionDTO(bo));
    }

    @Override
    @GetMapping("/sessions/{id}")
    public Response<DebugSessionDTO> sessionDetail(@PathVariable("id") String id) {
        DebugSessionBO bo = trackingDebugService.sessionDetail(id);
        return Response.success(TrackingDebugAdapterConvert.INSTANCE.toClientSessionDTO(bo));
    }

    @Override
    @PostMapping("/events/page")
    public Response<PageResultDTO<DebugEventSampleDTO>> eventPage(@RequestBody DebugEventPageQuery query) {
        var page = trackingDebugService.eventPage(TrackingDebugAdapterConvert.INSTANCE.toPageQuery(query));
        return Response.success(new PageResultDTO<>(
                page.getData().stream().map(TrackingDebugAdapterConvert.INSTANCE::toClientEventSampleDTO).toList(),
                page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @Override
    @GetMapping("/events/{id}")
    public Response<DebugEventSampleDTO> eventDetail(@PathVariable("id") String id) {
        DebugEventSampleBO bo = trackingDebugService.eventDetail(id);
        return Response.success(TrackingDebugAdapterConvert.INSTANCE.toClientEventSampleDTO(bo));
    }
}

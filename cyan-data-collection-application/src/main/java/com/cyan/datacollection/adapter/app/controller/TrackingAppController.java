package com.cyan.datacollection.adapter.app.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.app.controller.convert.TrackingAppAdapterConvert;
import com.cyan.datacollection.application.app.TrackingAppService;
import com.cyan.datacollection.application.app.bo.TrackingAppBO;
import com.cyan.datacollection.application.app.cmd.TrackingAppCmd;
import com.cyan.datacollection.adapter.app.controller.TrackingAppClient;
import com.cyan.datacollection.adapter.app.controller.dto.TrackingAppDTO;
import com.cyan.datacollection.adapter.app.controller.dto.TrackingAppIntegrationDTO;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.app.controller.request.TrackingAppCreateRequest;
import com.cyan.datacollection.adapter.app.controller.request.TrackingAppPageQuery;
import com.cyan.datacollection.adapter.app.controller.request.TrackingAppUpdateRequest;
import com.cyan.employee.login.filter.UserContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 接入应用控制器
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/apps")
public class TrackingAppController implements TrackingAppClient {

    private final TrackingAppService trackingAppService;

    public TrackingAppController(TrackingAppService trackingAppService) {
        this.trackingAppService = trackingAppService;
    }

    @Override
    @PostMapping("/page")
    public Response<PageResultDTO<TrackingAppDTO>> page(@RequestBody TrackingAppPageQuery query) {
        var page = trackingAppService.page(TrackingAppAdapterConvert.INSTANCE.toPageQuery(query));
        return Response.success(new PageResultDTO<>(
                page.getData().stream().map(TrackingAppAdapterConvert.INSTANCE::toClientDTO).toList(),
                page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @Override
    @PostMapping
    public Response<TrackingAppDTO> create(@RequestBody @Valid TrackingAppCreateRequest request) {
        TrackingAppCmd cmd = TrackingAppAdapterConvert.INSTANCE.toCmd(request);
        cmd.setCreateBy(UserContextHolder.getCurrentEmployee().getPassport());
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingAppBO bo = trackingAppService.create(cmd);
        return Response.success(TrackingAppAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PutMapping("/{id}")
    public Response<TrackingAppDTO> update(@PathVariable("id") String id, @RequestBody @Valid TrackingAppUpdateRequest request) {
        TrackingAppCmd cmd = TrackingAppAdapterConvert.INSTANCE.toCmd(request);
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingAppBO bo = trackingAppService.update(id, cmd);
        return Response.success(TrackingAppAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @GetMapping("/{id}")
    public Response<TrackingAppDTO> detail(@PathVariable("id") String id) {
        TrackingAppBO bo = trackingAppService.detail(id);
        return Response.success(TrackingAppAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/{id}/secret/rotate")
    public Response<TrackingAppDTO> rotateSecret(@PathVariable("id") String id) {
        TrackingAppBO bo = trackingAppService.rotateSecret(id);
        return Response.success(TrackingAppAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @GetMapping("/{id}/integration-code")
    public Response<TrackingAppIntegrationDTO> integrationCode(@PathVariable("id") String id) {
        TrackingAppBO.IntegrationBO bo = trackingAppService.integrationCode(id);
        return Response.success(TrackingAppAdapterConvert.INSTANCE.toClientIntegrationDTO(bo));
    }
}

package com.cyan.datacollection.adapter.workbench.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.workbench.controller.convert.TrackingWorkbenchAdapterConvert;
import com.cyan.datacollection.adapter.workbench.controller.dto.WorkbenchQualityRiskDTO;
import com.cyan.datacollection.adapter.workbench.controller.dto.WorkbenchSummaryDTO;
import com.cyan.datacollection.adapter.workbench.controller.dto.WorkbenchTodoDTO;
import com.cyan.datacollection.application.workbench.TrackingWorkbenchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工作台控制器
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/workbench")
public class TrackingWorkbenchController {

    private final TrackingWorkbenchService trackingWorkbenchService;

    public TrackingWorkbenchController(TrackingWorkbenchService trackingWorkbenchService) {
        this.trackingWorkbenchService = trackingWorkbenchService;
    }

    @GetMapping("/summary")
    public Response<WorkbenchSummaryDTO> summary() {
        var bo = trackingWorkbenchService.summary();
        return Response.success(TrackingWorkbenchAdapterConvert.INSTANCE.toDTO(bo));
    }

    @GetMapping("/todos")
    public Response<List<WorkbenchTodoDTO>> todos() {
        var list = trackingWorkbenchService.todos();
        return Response.success(TrackingWorkbenchAdapterConvert.INSTANCE.toTodoDTOList(list));
    }

    @GetMapping("/quality-risks")
    public Response<List<WorkbenchQualityRiskDTO>> qualityRisks() {
        var list = trackingWorkbenchService.qualityRisks();
        return Response.success(TrackingWorkbenchAdapterConvert.INSTANCE.toRiskDTOList(list));
    }
}

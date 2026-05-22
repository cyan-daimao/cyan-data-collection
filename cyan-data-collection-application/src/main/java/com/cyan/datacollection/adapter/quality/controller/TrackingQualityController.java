package com.cyan.datacollection.adapter.quality.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.quality.controller.convert.TrackingQualityAdapterConvert;
import com.cyan.datacollection.adapter.quality.controller.dto.QualityOverviewDTO;
import com.cyan.datacollection.adapter.quality.controller.dto.QualityTrendDTO;
import com.cyan.datacollection.adapter.quality.controller.dto.TrackingAlertDTO;
import com.cyan.datacollection.adapter.quality.controller.request.QualityOverviewRequest;
import com.cyan.datacollection.adapter.quality.controller.request.QualityTrendRequest;
import com.cyan.datacollection.adapter.quality.controller.request.TrackingAlertPageRequest;
import com.cyan.datacollection.application.quality.TrackingQualityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 质量监控控制器
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/quality")
public class TrackingQualityController {

    private final TrackingQualityService trackingQualityService;

    public TrackingQualityController(TrackingQualityService trackingQualityService) {
        this.trackingQualityService = trackingQualityService;
    }

    @PostMapping("/events/overview")
    public Response<List<QualityOverviewDTO>> eventsOverview(@RequestBody QualityOverviewRequest request) {
        var list = trackingQualityService.eventsOverview(TrackingQualityAdapterConvert.INSTANCE.toQuery(request));
        return Response.success(TrackingQualityAdapterConvert.INSTANCE.toOverviewDTOList(list));
    }

    @PostMapping("/events/trend")
    public Response<List<QualityTrendDTO>> eventsTrend(@RequestBody QualityTrendRequest request) {
        var list = trackingQualityService.eventsTrend(TrackingQualityAdapterConvert.INSTANCE.toQuery(request));
        return Response.success(TrackingQualityAdapterConvert.INSTANCE.toTrendDTOList(list));
    }

    @PostMapping("/alerts/page")
    public Response<PageResultDTO<TrackingAlertDTO>> alertPage(@RequestBody TrackingAlertPageRequest request) {
        var page = trackingQualityService.alertPage(TrackingQualityAdapterConvert.INSTANCE.toPageQuery(request));
        return Response.success(new PageResultDTO<>(
                TrackingQualityAdapterConvert.INSTANCE.toAlertDTOList(page.getData()),
                page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @PostMapping("/alerts/{id}/close")
    public Response<TrackingAlertDTO> closeAlert(@PathVariable("id") String id) {
        var alert = trackingQualityService.closeAlert(id);
        return Response.success(TrackingQualityAdapterConvert.INSTANCE.toDTO(alert));
    }
}

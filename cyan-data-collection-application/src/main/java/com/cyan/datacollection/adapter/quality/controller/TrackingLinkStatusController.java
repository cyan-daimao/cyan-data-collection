package com.cyan.datacollection.adapter.quality.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.application.quality.TrackingLinkStatusService;
import com.cyan.datacollection.application.quality.bo.LinkStatusBO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 采集链路状态控制器
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/quality")
public class TrackingLinkStatusController {

    private final TrackingLinkStatusService trackingLinkStatusService;

    public TrackingLinkStatusController(TrackingLinkStatusService trackingLinkStatusService) {
        this.trackingLinkStatusService = trackingLinkStatusService;
    }

    /**
     * 查询采集链路状态
     */
    @GetMapping("/link-status")
    public Response<LinkStatusBO> linkStatus() {
        return Response.success(trackingLinkStatusService.getLinkStatus());
    }
}

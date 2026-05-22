package com.cyan.datacollection.adapter.collect.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.collect.controller.convert.TrackingCollectAdapterConvert;
import com.cyan.datacollection.application.collect.TrackingCollectService;
import com.cyan.datacollection.application.collect.bo.CollectResultBO;
import com.cyan.datacollection.application.collect.cmd.EventCollectCmd;
import com.cyan.datacollection.adapter.collect.controller.TrackingCollectClient;
import com.cyan.datacollection.adapter.collect.controller.dto.CollectResultDTO;
import com.cyan.datacollection.adapter.collect.controller.request.EventCollectRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 事件上报控制器
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/collect")
public class TrackingCollectController implements TrackingCollectClient {

    private final TrackingCollectService trackingCollectService;

    public TrackingCollectController(TrackingCollectService trackingCollectService) {
        this.trackingCollectService = trackingCollectService;
    }

    @Override
    @PostMapping("/events")
    public Response<CollectResultDTO> collect(@RequestBody EventCollectRequest request) {
        EventCollectCmd cmd = TrackingCollectAdapterConvert.INSTANCE.toCmd(request);
        CollectResultBO bo = trackingCollectService.collect(cmd);
        return Response.success(TrackingCollectAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    @Override
    @PostMapping("/events/batch")
    public Response<List<CollectResultDTO>> collectBatch(@RequestBody List<EventCollectRequest> requests) {
        List<EventCollectCmd> cmds = requests.stream()
                .map(TrackingCollectAdapterConvert.INSTANCE::toCmd)
                .toList();
        List<CollectResultBO> bos = trackingCollectService.collectBatch(cmds);
        return Response.success(bos.stream()
                .map(TrackingCollectAdapterConvert.INSTANCE::toClientDTO)
                .toList());
    }
}

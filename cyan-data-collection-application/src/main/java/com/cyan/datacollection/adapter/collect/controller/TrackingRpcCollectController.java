package com.cyan.datacollection.adapter.collect.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.collect.controller.convert.TrackingCollectAdapterConvert;
import com.cyan.datacollection.adapter.collect.controller.dto.CollectResultDTO;
import com.cyan.datacollection.adapter.collect.controller.request.EventCollectRequest;
import com.cyan.datacollection.application.collect.TrackingCollectService;
import com.cyan.datacollection.application.collect.bo.CollectResultBO;
import com.cyan.datacollection.application.collect.cmd.EventCollectCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 事件上报开放接口（RPC）
 * 不依赖用户登录态，供前端无登录埋点上报使用
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/rpc/data-collection/collect")
public class TrackingRpcCollectController {

    private final TrackingCollectService trackingCollectService;

    public TrackingRpcCollectController(TrackingCollectService trackingCollectService) {
        this.trackingCollectService = trackingCollectService;
    }

    /**
     * 单条事件上报
     */
    @PostMapping("/events")
    public Response<CollectResultDTO> collect(@RequestBody EventCollectRequest request) {
        EventCollectCmd cmd = TrackingCollectAdapterConvert.INSTANCE.toCmd(request);
        CollectResultBO bo = trackingCollectService.collect(cmd);
        return Response.success(TrackingCollectAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    /**
     * 批量事件上报
     */
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

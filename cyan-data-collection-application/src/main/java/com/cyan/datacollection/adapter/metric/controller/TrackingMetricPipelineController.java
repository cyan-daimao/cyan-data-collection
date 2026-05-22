package com.cyan.datacollection.adapter.metric.controller;

import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.metric.controller.convert.TrackingMetricPipelineAdapterConvert;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.metric.controller.dto.TrackingMetricPipelineDTO;
import com.cyan.datacollection.adapter.metric.controller.request.TrackingMetricPipelineCreateRequest;
import com.cyan.datacollection.adapter.metric.controller.request.TrackingMetricPipelinePageQueryRequest;
import com.cyan.datacollection.application.metric.TrackingMetricPipelineService;
import com.cyan.datacollection.application.metric.bo.TrackingMetricPipelineBO;
import com.cyan.datacollection.application.metric.cmd.TrackingMetricPipelineCmd;
import com.cyan.datacollection.domain.metric.query.TrackingMetricPipelinePageQuery;
import com.cyan.employee.login.filter.UserContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 采集指标链路接口
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/metric-pipelines")
public class TrackingMetricPipelineController {

    private final TrackingMetricPipelineService service;

    public TrackingMetricPipelineController(TrackingMetricPipelineService service) {
        this.service = service;
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    public Response<PageResultDTO<TrackingMetricPipelineDTO>> page(@RequestBody TrackingMetricPipelinePageQueryRequest request) {
        com.cyan.datacollection.domain.metric.query.TrackingMetricPipelinePageQuery query = new com.cyan.datacollection.domain.metric.query.TrackingMetricPipelinePageQuery();
        query.setPageNum(request.getPageNum());
        query.setPageSize(request.getPageSize());
        query.setMetricCode(request.getMetricCode());
        query.setMetricName(request.getMetricName());
        query.setStatus(request.getStatus());
        Page<TrackingMetricPipelineBO> page = service.page(query);
        List<TrackingMetricPipelineDTO> data = Optional.ofNullable(page.getData()).orElse(List.of()).stream()
                .map(TrackingMetricPipelineAdapterConvert.INSTANCE::toTrackingMetricPipelineDTO)
                .toList();
        PageResultDTO<TrackingMetricPipelineDTO> result = new PageResultDTO<>();
        result.setList(data);
        result.setTotal(page.getTotal());
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        return Response.success(result);
    }

    /**
     * 创建
     */
    @PostMapping
    public Response<TrackingMetricPipelineDTO> create(@RequestBody @Valid TrackingMetricPipelineCreateRequest request) {
        TrackingMetricPipelineCmd cmd = new TrackingMetricPipelineCmd()
                .setMetricCode(request.getMetricCode())
                .setMetricName(request.getMetricName())
                .setEventCode(request.getEventCode())
                .setAppCode(request.getAppCode())
                .setDimensions(request.getDimensions())
                .setMeasures(Optional.ofNullable(request.getMeasures()).orElse(List.of()).stream()
                        .map(m -> new TrackingMetricPipelineCmd.MeasureCmd(m.getName(), m.getExpr()))
                        .toList());
        String createdBy = UserContextHolder.getCurrentEmployee().getPassport();
        TrackingMetricPipelineBO bo = service.create(cmd, createdBy);
        TrackingMetricPipelineDTO dto = TrackingMetricPipelineAdapterConvert.INSTANCE.toTrackingMetricPipelineDTO(bo);
        return Response.success(dto);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public Response<TrackingMetricPipelineDTO> detail(@PathVariable String id) {
        TrackingMetricPipelineBO bo = service.detail(id);
        TrackingMetricPipelineDTO dto = TrackingMetricPipelineAdapterConvert.INSTANCE.toTrackingMetricPipelineDTO(bo);
        return Response.success(dto);
    }

    /**
     * 创建表和链路
     */
    @PostMapping("/{id}/provision")
    public Response<TrackingMetricPipelineDTO> provision(@PathVariable String id) {
        TrackingMetricPipelineBO bo = service.provision(id);
        TrackingMetricPipelineDTO dto = TrackingMetricPipelineAdapterConvert.INSTANCE.toTrackingMetricPipelineDTO(bo);
        return Response.success(dto);
    }

    /**
     * 启动任务
     */
    @PostMapping("/{id}/start")
    public Response<TrackingMetricPipelineDTO> start(@PathVariable String id) {
        TrackingMetricPipelineBO bo = service.start(id);
        TrackingMetricPipelineDTO dto = TrackingMetricPipelineAdapterConvert.INSTANCE.toTrackingMetricPipelineDTO(bo);
        return Response.success(dto);
    }
}

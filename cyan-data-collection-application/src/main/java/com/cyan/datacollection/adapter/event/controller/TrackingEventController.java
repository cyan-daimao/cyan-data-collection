package com.cyan.datacollection.adapter.event.controller;

import com.cyan.arch.common.api.Response;
import com.cyan.datacollection.adapter.event.controller.convert.TrackingEventAdapterConvert;
import com.cyan.datacollection.adapter.mapping.controller.convert.TrackingMappingAdapterConvert;
import com.cyan.datacollection.adapter.mapping.controller.dto.EventMetricMappingDTO;
import com.cyan.datacollection.adapter.mapping.controller.request.EventMetricSyncRequest;
import com.cyan.datacollection.application.event.TrackingEventService;
import com.cyan.datacollection.application.event.bo.TrackingEventBO;
import com.cyan.datacollection.application.event.cmd.TrackingEventCmd;
import com.cyan.datacollection.application.eventproperty.TrackingEventPropertyService;
import com.cyan.datacollection.application.eventproperty.bo.EventPropertyBO;
import com.cyan.datacollection.application.eventproperty.cmd.EventPropertyConfigCmd;
import com.cyan.datacollection.application.mapping.TrackingMetricMappingService;
import com.cyan.datacollection.application.mapping.bo.EventMetricMappingBO;
import com.cyan.datacollection.adapter.common.PageResultDTO;
import com.cyan.datacollection.adapter.event.controller.dto.EventPropertyDTO;
import com.cyan.datacollection.adapter.event.controller.dto.TrackingEventDTO;
import com.cyan.datacollection.adapter.event.controller.dto.TrackingEventUsageDTO;
import com.cyan.datacollection.adapter.event.controller.request.EventPropertyConfigRequest;
import com.cyan.datacollection.adapter.event.controller.request.TrackingEventCreateRequest;
import com.cyan.datacollection.adapter.event.controller.request.TrackingEventPageQuery;
import com.cyan.datacollection.adapter.event.controller.request.TrackingEventUpdateRequest;
import com.cyan.employee.login.filter.UserContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件定义控制器
 * API: ready
 *
 * @author cy.Y
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/data-collection/events")
public class TrackingEventController  {

    private final TrackingEventService trackingEventService;
    private final TrackingEventPropertyService trackingEventPropertyService;
    private final TrackingMetricMappingService trackingMetricMappingService;

    public TrackingEventController(TrackingEventService trackingEventService,
                                   TrackingEventPropertyService trackingEventPropertyService,
                                   TrackingMetricMappingService trackingMetricMappingService) {
        this.trackingEventService = trackingEventService;
        this.trackingEventPropertyService = trackingEventPropertyService;
        this.trackingMetricMappingService = trackingMetricMappingService;
    }

    
    @PostMapping("/page")
    public Response<PageResultDTO<TrackingEventDTO>> page(@RequestBody TrackingEventPageQuery query) {
        var page = trackingEventService.page(TrackingEventAdapterConvert.INSTANCE.toPageQuery(query));
        return Response.success(new PageResultDTO<>(
                page.getData().stream().map(TrackingEventAdapterConvert.INSTANCE::toClientDTO).toList(),
                page.getTotal(), page.getCurrent(), page.getSize()));
    }

    
    @PostMapping
    public Response<TrackingEventDTO> create(@RequestBody @Valid TrackingEventCreateRequest request) {
        TrackingEventCmd cmd = TrackingEventAdapterConvert.INSTANCE.toCmd(request);
        cmd.setCreateBy(UserContextHolder.getCurrentEmployee().getPassport());
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingEventBO bo = trackingEventService.create(cmd);
        return Response.success(TrackingEventAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    
    @PutMapping("/{id}")
    public Response<TrackingEventDTO> update(@PathVariable("id") String id, @RequestBody @Valid TrackingEventUpdateRequest request) {
        TrackingEventCmd cmd = TrackingEventAdapterConvert.INSTANCE.toCmd(request);
        cmd.setUpdateBy(UserContextHolder.getCurrentEmployee().getPassport());
        TrackingEventBO bo = trackingEventService.update(id, cmd);
        return Response.success(TrackingEventAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    
    @GetMapping("/{id}")
    public Response<TrackingEventDTO> detail(@PathVariable("id") String id) {
        TrackingEventBO bo = trackingEventService.detail(id);
        return Response.success(TrackingEventAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    
    @PostMapping("/{id}/publish")
    public Response<TrackingEventDTO> publish(@PathVariable("id") String id) {
        TrackingEventBO bo = trackingEventService.publish(id);
        return Response.success(TrackingEventAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    
    @PostMapping("/{id}/deprecate")
    public Response<TrackingEventDTO> deprecate(@PathVariable("id") String id) {
        TrackingEventBO bo = trackingEventService.deprecate(id);
        return Response.success(TrackingEventAdapterConvert.INSTANCE.toClientDTO(bo));
    }

    
    @GetMapping("/{id}/usage")
    public Response<TrackingEventUsageDTO> usage(@PathVariable("id") String id) {
        TrackingEventBO.UsageBO usage = trackingEventService.usage(id);
        return Response.success(TrackingEventAdapterConvert.INSTANCE.toClientUsageDTO(usage));
    }

    
    @PutMapping("/{id}/properties")
    public Response<Void> configProperties(@PathVariable("id") String id,
                                           @RequestBody @Valid List<EventPropertyConfigRequest> requests) {
        List<EventPropertyConfigCmd> cmds = requests.stream()
                .map(TrackingEventAdapterConvert.INSTANCE::toCmd)
                .toList();
        trackingEventPropertyService.configProperties(id, cmds);
        return Response.success(null);
    }

    
    @GetMapping("/{id}/properties")
    public Response<List<EventPropertyDTO>> listProperties(@PathVariable("id") String id) {
        List<EventPropertyBO> bos = trackingEventPropertyService.listProperties(id);
        return Response.success(bos.stream()
                .map(TrackingEventAdapterConvert.INSTANCE::toEventPropertyDTO)
                .toList());
    }

    
    @PostMapping("/{id}/sync-metric")
    public Response<EventMetricMappingDTO> syncMetric(@PathVariable("id") String id,
                                                      @RequestBody EventMetricSyncRequest request) {
        var cmd = TrackingMappingAdapterConvert.INSTANCE.toCmd(request == null ? new EventMetricSyncRequest() : request);
        cmd.setOperator(UserContextHolder.getCurrentEmployee().getPassport());
        EventMetricMappingBO bo = trackingMetricMappingService.syncEventMetric(id, cmd);
        return Response.success(TrackingMappingAdapterConvert.INSTANCE.toDTO(bo));
    }

    
    @GetMapping("/{id}/metric-mapping")
    public Response<EventMetricMappingDTO> metricMapping(@PathVariable("id") String id) {
        EventMetricMappingBO bo = trackingMetricMappingService.getEventMetricMapping(id);
        return Response.success(TrackingMappingAdapterConvert.INSTANCE.toDTO(bo));
    }
}

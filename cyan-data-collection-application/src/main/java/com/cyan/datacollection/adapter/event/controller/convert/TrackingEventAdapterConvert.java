package com.cyan.datacollection.adapter.event.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.event.bo.TrackingEventBO;
import com.cyan.datacollection.application.event.cmd.TrackingEventCmd;
import com.cyan.datacollection.application.eventproperty.bo.EventPropertyBO;
import com.cyan.datacollection.application.eventproperty.cmd.EventPropertyConfigCmd;
import com.cyan.datacollection.adapter.event.controller.dto.EventPropertyDTO;
import com.cyan.datacollection.adapter.event.controller.dto.TrackingEventDTO;
import com.cyan.datacollection.adapter.event.controller.dto.TrackingEventUsageDTO;
import com.cyan.datacollection.adapter.event.controller.request.EventPropertyConfigRequest;
import com.cyan.datacollection.adapter.event.controller.request.TrackingEventCreateRequest;
import com.cyan.datacollection.adapter.event.controller.request.TrackingEventUpdateRequest;
import com.cyan.datacollection.domain.event.query.TrackingEventPageQuery;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 事件定义适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingEventAdapterConvert {

    TrackingEventAdapterConvert INSTANCE = Mappers.getMapper(TrackingEventAdapterConvert.class);

    TrackingEventDTO toClientDTO(TrackingEventBO bo);

    List<TrackingEventDTO> toClientDTOList(List<TrackingEventBO> bos);

    TrackingEventCmd toCmd(TrackingEventCreateRequest request);

    TrackingEventCmd toCmd(TrackingEventUpdateRequest request);

    TrackingEventPageQuery toPageQuery(com.cyan.datacollection.adapter.event.controller.request.TrackingEventPageQuery query);

    EventPropertyConfigCmd toCmd(EventPropertyConfigRequest request);

    EventPropertyDTO toEventPropertyDTO(EventPropertyBO bo);

    default TrackingEventUsageDTO toClientUsageDTO(TrackingEventBO.UsageBO usage) {
        if (usage == null) {
            return null;
        }
        TrackingEventUsageDTO dto = new TrackingEventUsageDTO();
        dto.setEventId(usage.getEventId());
        dto.setPlanCount(usage.getPlanCount());
        if (usage.getPlans() != null) {
            dto.setPlans(usage.getPlans().stream().map(bo -> {
                com.cyan.datacollection.adapter.event.controller.dto.TrackingEventPlanRefDTO plan = new com.cyan.datacollection.adapter.event.controller.dto.TrackingEventPlanRefDTO();
                plan.setPlanId(bo.getPlanId());
                plan.setPlanCode(bo.getPlanCode());
                plan.setPlanName(bo.getPlanName());
                return plan;
            }).toList());
        }
        dto.setRecentSampleCount(usage.getRecentSampleCount());
        dto.setPropertyCount(usage.getPropertyCount());
        return dto;
    }
}

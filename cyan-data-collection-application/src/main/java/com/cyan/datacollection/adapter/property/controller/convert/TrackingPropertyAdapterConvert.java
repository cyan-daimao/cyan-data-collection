package com.cyan.datacollection.adapter.property.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.property.bo.TrackingPropertyBO;
import com.cyan.datacollection.application.property.cmd.TrackingPropertyCmd;
import com.cyan.datacollection.adapter.property.controller.dto.TrackingPropertyDTO;
import com.cyan.datacollection.adapter.property.controller.dto.TrackingPropertyUsageDTO;
import com.cyan.datacollection.adapter.property.controller.request.TrackingPropertyCreateRequest;
import com.cyan.datacollection.adapter.property.controller.request.TrackingPropertyUpdateRequest;
import com.cyan.datacollection.domain.property.query.TrackingPropertyPageQuery;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 属性定义适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingPropertyAdapterConvert {

    TrackingPropertyAdapterConvert INSTANCE = Mappers.getMapper(TrackingPropertyAdapterConvert.class);

    TrackingPropertyDTO toClientDTO(TrackingPropertyBO bo);

    List<TrackingPropertyDTO> toClientDTOList(List<TrackingPropertyBO> bos);

    TrackingPropertyCmd toCmd(TrackingPropertyCreateRequest request);

    TrackingPropertyCmd toCmd(TrackingPropertyUpdateRequest request);

    TrackingPropertyPageQuery toPageQuery(com.cyan.datacollection.adapter.property.controller.request.TrackingPropertyPageQuery query);

    default TrackingPropertyUsageDTO toClientUsageDTO(TrackingPropertyBO.UsageBO usage) {
        if (usage == null) {
            return null;
        }
        TrackingPropertyUsageDTO dto = new TrackingPropertyUsageDTO();
        dto.setPropertyId(usage.getPropertyId());
        dto.setEventCount(usage.getEventCount());
        if (usage.getEvents() != null) {
            dto.setEvents(usage.getEvents().stream().map(bo -> {
                com.cyan.datacollection.adapter.property.controller.dto.TrackingPropertyEventRefDTO event = new com.cyan.datacollection.adapter.property.controller.dto.TrackingPropertyEventRefDTO();
                event.setEventId(bo.getEventId());
                event.setEventCode(bo.getEventCode());
                event.setEventName(bo.getEventName());
                return event;
            }).toList());
        }
        return dto;
    }
}

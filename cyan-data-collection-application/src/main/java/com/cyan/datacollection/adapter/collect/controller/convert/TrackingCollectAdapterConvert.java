package com.cyan.datacollection.adapter.collect.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.collect.bo.CollectResultBO;
import com.cyan.datacollection.application.collect.cmd.EventCollectCmd;
import com.cyan.datacollection.adapter.collect.controller.dto.CollectResultDTO;
import com.cyan.datacollection.adapter.collect.controller.request.EventCollectRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 事件上报适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingCollectAdapterConvert {

    TrackingCollectAdapterConvert INSTANCE = Mappers.getMapper(TrackingCollectAdapterConvert.class);

    CollectResultDTO toClientDTO(CollectResultBO bo);

    EventCollectCmd toCmd(EventCollectRequest request);
}

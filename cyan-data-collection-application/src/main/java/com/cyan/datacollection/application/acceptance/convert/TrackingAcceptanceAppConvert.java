package com.cyan.datacollection.application.acceptance.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.acceptance.bo.TrackingAcceptanceResultBO;
import com.cyan.datacollection.application.acceptance.bo.TrackingAcceptanceTaskBO;
import com.cyan.datacollection.application.acceptance.cmd.TrackingAcceptanceTaskCmd;
import com.cyan.datacollection.domain.acceptance.TrackingAcceptanceResult;
import com.cyan.datacollection.domain.acceptance.TrackingAcceptanceTask;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 验收任务应用层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingAcceptanceAppConvert {

    TrackingAcceptanceAppConvert INSTANCE = Mappers.getMapper(TrackingAcceptanceAppConvert.class);

    TrackingAcceptanceTaskBO toTaskBO(TrackingAcceptanceTask task);

    TrackingAcceptanceTask toTaskDomain(TrackingAcceptanceTaskCmd cmd);

    TrackingAcceptanceResultBO toResultBO(TrackingAcceptanceResult result);

    List<TrackingAcceptanceResultBO> toResultBOList(List<TrackingAcceptanceResult> results);
}

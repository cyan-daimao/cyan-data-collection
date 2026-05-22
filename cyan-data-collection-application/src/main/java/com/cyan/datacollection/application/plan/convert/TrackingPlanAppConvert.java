package com.cyan.datacollection.application.plan.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.plan.bo.TrackingPlanBO;
import com.cyan.datacollection.application.plan.cmd.TrackingPlanCmd;
import com.cyan.datacollection.domain.plan.TrackingPlan;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 埋点方案应用层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingPlanAppConvert {

    TrackingPlanAppConvert INSTANCE = Mappers.getMapper(TrackingPlanAppConvert.class);

    TrackingPlanBO toBO(TrackingPlan plan);

    TrackingPlan toDomain(TrackingPlanCmd cmd);
}

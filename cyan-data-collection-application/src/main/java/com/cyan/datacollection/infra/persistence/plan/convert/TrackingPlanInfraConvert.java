package com.cyan.datacollection.infra.persistence.plan.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.plan.TrackingPlan;
import com.cyan.datacollection.infra.persistence.plan.dos.TrackingPlanDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 埋点方案转换器
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingPlanInfraConvert {

    TrackingPlanInfraConvert INSTANCE = Mappers.getMapper(TrackingPlanInfraConvert.class);

    TrackingPlan toDomain(TrackingPlanDO dos);

    TrackingPlanDO toDO(TrackingPlan domain);
}

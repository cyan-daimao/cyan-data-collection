package com.cyan.datacollection.infra.persistence.plan.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.plan.TrackingPlanEventRelation;
import com.cyan.datacollection.infra.persistence.plan.dos.TrackingPlanEventDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 方案事件关系转换器
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingPlanEventInfraConvert {

    TrackingPlanEventInfraConvert INSTANCE = Mappers.getMapper(TrackingPlanEventInfraConvert.class);

    /**
     * DO 转领域模型
     */
    TrackingPlanEventRelation toRelation(TrackingPlanEventDO dos);

    /**
     * 领域模型转 DO
     */
    TrackingPlanEventDO toDO(TrackingPlanEventRelation relation);
}

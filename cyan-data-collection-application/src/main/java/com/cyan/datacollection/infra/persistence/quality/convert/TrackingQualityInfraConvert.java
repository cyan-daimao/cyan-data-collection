package com.cyan.datacollection.infra.persistence.quality.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.quality.TrackingAlert;
import com.cyan.datacollection.domain.quality.TrackingQualityMetric;
import com.cyan.datacollection.infra.persistence.quality.dos.TrackingAlertDO;
import com.cyan.datacollection.infra.persistence.quality.dos.TrackingQualityMetricDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 质量监控转换器
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingQualityInfraConvert {

    TrackingQualityInfraConvert INSTANCE = Mappers.getMapper(TrackingQualityInfraConvert.class);

    TrackingQualityMetric toDomain(TrackingQualityMetricDO dos);

    TrackingQualityMetricDO toDO(TrackingQualityMetric domain);

    TrackingAlert toDomain(TrackingAlertDO dos);

    TrackingAlertDO toDO(TrackingAlert domain);
}

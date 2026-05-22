package com.cyan.datacollection.infra.persistence.release.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.release.TrackingRelease;
import com.cyan.datacollection.domain.release.TrackingReleaseItem;
import com.cyan.datacollection.infra.persistence.release.dos.TrackingReleaseDO;
import com.cyan.datacollection.infra.persistence.release.dos.TrackingReleaseItemDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 埋点发布版本转换器
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingReleaseInfraConvert {

    TrackingReleaseInfraConvert INSTANCE = Mappers.getMapper(TrackingReleaseInfraConvert.class);

    TrackingRelease toDomain(TrackingReleaseDO dos);

    TrackingReleaseDO toDO(TrackingRelease domain);

    TrackingReleaseItem toDomain(TrackingReleaseItemDO dos);

    TrackingReleaseItemDO toDO(TrackingReleaseItem domain);
}

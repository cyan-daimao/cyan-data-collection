package com.cyan.datacollection.application.release.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.application.release.bo.TrackingReleaseBO;
import com.cyan.datacollection.application.release.cmd.TrackingReleaseCmd;
import com.cyan.datacollection.domain.release.TrackingRelease;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 埋点发布版本应用层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingReleaseAppConvert {

    TrackingReleaseAppConvert INSTANCE = Mappers.getMapper(TrackingReleaseAppConvert.class);

    TrackingReleaseBO toBO(TrackingRelease release);

    TrackingRelease toDomain(TrackingReleaseCmd cmd);
}

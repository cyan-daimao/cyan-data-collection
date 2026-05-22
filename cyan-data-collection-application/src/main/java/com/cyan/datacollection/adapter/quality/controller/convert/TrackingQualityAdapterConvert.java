package com.cyan.datacollection.adapter.quality.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.adapter.quality.controller.dto.QualityOverviewDTO;
import com.cyan.datacollection.adapter.quality.controller.dto.QualityTrendDTO;
import com.cyan.datacollection.adapter.quality.controller.dto.TrackingAlertDTO;
import com.cyan.datacollection.adapter.quality.controller.request.QualityOverviewRequest;
import com.cyan.datacollection.adapter.quality.controller.request.QualityTrendRequest;
import com.cyan.datacollection.adapter.quality.controller.request.TrackingAlertPageRequest;
import com.cyan.datacollection.application.quality.bo.QualityOverviewBO;
import com.cyan.datacollection.application.quality.bo.QualityTrendBO;
import com.cyan.datacollection.application.quality.cmd.QualityOverviewQuery;
import com.cyan.datacollection.application.quality.cmd.QualityTrendQuery;
import com.cyan.datacollection.domain.quality.query.TrackingAlertPageQuery;
import com.cyan.datacollection.domain.quality.TrackingAlert;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 质量监控适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingQualityAdapterConvert {

    TrackingQualityAdapterConvert INSTANCE = Mappers.getMapper(TrackingQualityAdapterConvert.class);

    QualityOverviewDTO toDTO(QualityOverviewBO bo);

    List<QualityOverviewDTO> toOverviewDTOList(List<QualityOverviewBO> bos);

    QualityTrendDTO toDTO(QualityTrendBO bo);

    List<QualityTrendDTO> toTrendDTOList(List<QualityTrendBO> bos);

    TrackingAlertDTO toDTO(TrackingAlert alert);

    List<TrackingAlertDTO> toAlertDTOList(List<TrackingAlert> alerts);

    QualityOverviewQuery toQuery(QualityOverviewRequest request);

    QualityTrendQuery toQuery(QualityTrendRequest request);

    TrackingAlertPageQuery toPageQuery(TrackingAlertPageRequest request);
}

package com.cyan.datacollection.adapter.workbench.controller.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.adapter.workbench.controller.dto.WorkbenchQualityRiskDTO;
import com.cyan.datacollection.adapter.workbench.controller.dto.WorkbenchSummaryDTO;
import com.cyan.datacollection.adapter.workbench.controller.dto.WorkbenchTodoDTO;
import com.cyan.datacollection.application.workbench.bo.WorkbenchQualityRiskBO;
import com.cyan.datacollection.application.workbench.bo.WorkbenchSummaryBO;
import com.cyan.datacollection.application.workbench.bo.WorkbenchTodoBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 工作台适配层转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingWorkbenchAdapterConvert {

    TrackingWorkbenchAdapterConvert INSTANCE = Mappers.getMapper(TrackingWorkbenchAdapterConvert.class);

    WorkbenchSummaryDTO toDTO(WorkbenchSummaryBO bo);

    WorkbenchTodoDTO toDTO(WorkbenchTodoBO bo);

    List<WorkbenchTodoDTO> toTodoDTOList(List<WorkbenchTodoBO> bos);

    WorkbenchQualityRiskDTO toDTO(WorkbenchQualityRiskBO bo);

    List<WorkbenchQualityRiskDTO> toRiskDTOList(List<WorkbenchQualityRiskBO> bos);
}

package com.cyan.datacollection.infra.persistence.acceptance.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.acceptance.TrackingAcceptanceResult;
import com.cyan.datacollection.domain.acceptance.TrackingAcceptanceTask;
import com.cyan.datacollection.infra.persistence.acceptance.dos.TrackingAcceptanceResultDO;
import com.cyan.datacollection.infra.persistence.acceptance.dos.TrackingAcceptanceTaskDO;
import com.alibaba.fastjson2.JSON;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 埋点验收转换器
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingAcceptanceInfraConvert {

    TrackingAcceptanceInfraConvert INSTANCE = Mappers.getMapper(TrackingAcceptanceInfraConvert.class);

    TrackingAcceptanceTask toTaskDomain(TrackingAcceptanceTaskDO dos);

    TrackingAcceptanceTaskDO toTaskDO(TrackingAcceptanceTask domain);

    TrackingAcceptanceResult toResultDomain(TrackingAcceptanceResultDO dos);

    TrackingAcceptanceResultDO toResultDO(TrackingAcceptanceResult domain);

    default List<String> toStringList(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSON.parseArray(json, String.class);
    }

    default String toJsonString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return JSON.toJSONString(list);
    }
}

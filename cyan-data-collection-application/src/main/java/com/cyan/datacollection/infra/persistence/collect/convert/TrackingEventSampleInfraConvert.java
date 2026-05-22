package com.cyan.datacollection.infra.persistence.collect.convert;

import com.cyan.arch.base.mapstruct.MapstructConvert;
import com.cyan.datacollection.domain.collect.TrackingEventSample;
import com.cyan.datacollection.infra.persistence.collect.dos.TrackingEventSampleDO;
import com.alibaba.fastjson2.JSON;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 事件样本转换
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper(componentModel = "spring", uses = MapstructConvert.class)
public interface TrackingEventSampleInfraConvert {

    TrackingEventSampleInfraConvert INSTANCE = Mappers.getMapper(TrackingEventSampleInfraConvert.class);

    TrackingEventSample toTrackingEventSample(TrackingEventSampleDO trackingEventSampleDO);

    TrackingEventSampleDO toTrackingEventSampleDO(TrackingEventSample trackingEventSample);

    default List<String> toValidateErrors(String validateErrors) {
        if (validateErrors == null || validateErrors.isEmpty()) {
            return null;
        }
        return JSON.parseArray(validateErrors, String.class);
    }

    default String toValidateErrorsString(List<String> validateErrors) {
        if (validateErrors == null || validateErrors.isEmpty()) {
            return null;
        }
        return JSON.toJSONString(validateErrors);
    }
}

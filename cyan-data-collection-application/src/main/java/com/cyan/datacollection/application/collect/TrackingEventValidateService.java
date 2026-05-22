package com.cyan.datacollection.application.collect;

import com.cyan.datacollection.application.collect.bo.ValidateResultBO;
import com.cyan.datacollection.domain.event.TrackingEvent;

import java.util.Map;

/**
 * 事件属性校验服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingEventValidateService {

    /**
     * 基于事件属性规则校验上报数据
     *
     * @param event      事件定义
     * @param properties 上报的属性Map
     * @return 校验结果（包含状态和错误明细）
     */
    ValidateResultBO validate(TrackingEvent event, Map<String, Object> properties);
}

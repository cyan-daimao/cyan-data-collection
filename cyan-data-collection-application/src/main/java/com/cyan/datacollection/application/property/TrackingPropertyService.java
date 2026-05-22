package com.cyan.datacollection.application.property;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.application.property.bo.TrackingPropertyBO;
import com.cyan.datacollection.application.property.cmd.TrackingPropertyCmd;
import com.cyan.datacollection.domain.property.query.TrackingPropertyPageQuery;

import java.util.List;

/**
 * 属性定义服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingPropertyService {

    Page<TrackingPropertyBO> page(TrackingPropertyPageQuery query);

    TrackingPropertyBO create(TrackingPropertyCmd cmd);

    TrackingPropertyBO update(String id, TrackingPropertyCmd cmd);

    TrackingPropertyBO detail(String id);

    TrackingPropertyBO publish(String id);

    TrackingPropertyBO deprecate(String id);

    TrackingPropertyBO.UsageBO usage(String id);

    List<TrackingPropertyBO> findByIds(List<String> ids);
}

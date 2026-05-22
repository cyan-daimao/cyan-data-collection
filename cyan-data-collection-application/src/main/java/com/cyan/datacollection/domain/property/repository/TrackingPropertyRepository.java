package com.cyan.datacollection.domain.property.repository;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.domain.property.TrackingProperty;
import com.cyan.datacollection.domain.property.query.TrackingPropertyPageQuery;

import java.util.List;

/**
 * 属性定义仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingPropertyRepository {

    TrackingProperty findById(String id);

    Page<TrackingProperty> page(TrackingPropertyPageQuery query);

    TrackingProperty findByCode(String propertyCode);

    TrackingProperty save(TrackingProperty property);

    TrackingProperty update(TrackingProperty property);

    void deleteById(String id);

    List<TrackingProperty> findByIds(List<String> ids);
}

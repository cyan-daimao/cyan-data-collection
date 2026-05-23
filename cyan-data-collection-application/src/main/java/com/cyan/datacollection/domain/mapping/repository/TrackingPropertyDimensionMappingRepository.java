package com.cyan.datacollection.domain.mapping.repository;

import com.cyan.datacollection.domain.mapping.TrackingPropertyDimensionMapping;

/**
 * 采集属性维度映射仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingPropertyDimensionMappingRepository {

    /**
     * 根据属性ID查询
     */
    TrackingPropertyDimensionMapping findByPropertyId(String propertyId);

    /**
     * 根据维度编码查询
     */
    TrackingPropertyDimensionMapping findByDimCode(String dimCode);

    /**
     * 保存
     */
    TrackingPropertyDimensionMapping save(TrackingPropertyDimensionMapping mapping);

    /**
     * 更新
     */
    TrackingPropertyDimensionMapping update(TrackingPropertyDimensionMapping mapping);
}

package com.cyan.datacollection.adapter.property.controller.request;

import lombok.Data;

/**
 * 属性分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingPropertyPageQuery {

    /**
     * 页码
     */
    private long pageNum = 1;

    /**
     * 页大小
     */
    private long pageSize = 20;

    /**
     * 属性编码（模糊）
     */
    private String propertyCode;

    /**
     * 属性名称（模糊）
     */
    private String propertyName;

    /**
     * 属性类型
     */
    private String propertyType;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 状态
     */
    private String status;
}

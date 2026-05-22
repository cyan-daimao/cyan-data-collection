package com.cyan.datacollection.adapter.demand.controller.request;

import lombok.Data;

/**
 * 需求分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingDemandPageQuery {

    /**
     * 页码
     */
    private long pageNum = 1;

    /**
     * 页大小
     */
    private long pageSize = 20;

    /**
     * 需求编号（模糊）
     */
    private String demandCode;

    /**
     * 需求名称（模糊）
     */
    private String demandName;

    /**
     * 业务域
     */
    private String businessDomain;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 状态
     */
    private String status;
}

package com.cyan.datacollection.application.demand.query;

import com.cyan.arch.common.api.Pageable;
import lombok.Data;

/**
 * 埋点需求分页查询（Application层）
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingDemandPageQuery implements Pageable {

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

    @Override
    public long current() {
        return pageNum;
    }

    @Override
    public long size() {
        return pageSize;
    }
}

package com.cyan.datacollection.domain.plan.query;

import com.cyan.arch.common.api.Pageable;
import lombok.Data;

/**
 * 埋点方案分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingPlanPageQuery implements Pageable {

    /**
     * 页码
     */
    private long pageNum = 1;

    /**
     * 页大小
     */
    private long pageSize = 20;

    /**
     * 方案编号（模糊）
     */
    private String planCode;

    /**
     * 方案名称（模糊）
     */
    private String planName;

    /**
     * 关联需求ID
     */
    private String demandId;

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

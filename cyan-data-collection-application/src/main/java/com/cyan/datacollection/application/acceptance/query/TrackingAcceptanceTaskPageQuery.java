package com.cyan.datacollection.application.acceptance.query;

import com.cyan.arch.common.api.Pageable;
import lombok.Data;

/**
 * 验收任务分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingAcceptanceTaskPageQuery implements Pageable {

    /**
     * 页码
     */
    private long pageNum = 1;

    /**
     * 页大小
     */
    private long pageSize = 20;

    /**
     * 验收任务编号（模糊）
     */
    private String taskCode;

    /**
     * 方案ID
     */
    private String planId;

    /**
     * 状态
     */
    private String status;

    /**
     * Debug Token
     */
    private String debugToken;

    @Override
    public long current() {
        return pageNum;
    }

    @Override
    public long size() {
        return pageSize;
    }
}

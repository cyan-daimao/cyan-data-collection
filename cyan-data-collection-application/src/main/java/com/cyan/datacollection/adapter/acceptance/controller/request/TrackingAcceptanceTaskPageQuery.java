package com.cyan.datacollection.adapter.acceptance.controller.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * 验收任务分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingAcceptanceTaskPageQuery {

    /**
     * 页码
     */
    @JsonAlias({"pageNo", "current"})
    private long pageNum = 1;

    /**
     * 页大小
     */
    @JsonAlias("size")
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
}

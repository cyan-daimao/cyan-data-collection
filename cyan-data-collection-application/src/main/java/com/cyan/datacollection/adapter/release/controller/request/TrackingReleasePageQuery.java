package com.cyan.datacollection.adapter.release.controller.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * 发布版本分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingReleasePageQuery {

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
     * 方案ID
     */
    private String planId;

    /**
     * 发布编号（模糊）
     */
    private String releaseCode;

    /**
     * 状态
     */
    private String status;
}

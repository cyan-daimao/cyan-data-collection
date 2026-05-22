package com.cyan.datacollection.adapter.app.controller.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * 应用分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingAppPageQuery {

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
     * 应用编码（模糊）
     */
    private String appCode;

    /**
     * 应用名称（模糊）
     */
    private String appName;

    /**
     * 应用类型
     */
    private String appType;

    /**
     * 状态
     */
    private String status;
}

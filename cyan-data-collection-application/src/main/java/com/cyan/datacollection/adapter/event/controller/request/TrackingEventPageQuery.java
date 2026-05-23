package com.cyan.datacollection.adapter.event.controller.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * 事件分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingEventPageQuery {

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
     * 应用编码
     */
    private String appCode;

    /**
     * 事件编码（模糊）
     */
    private String eventCode;

    /**
     * 事件名称（模糊）
     */
    private String eventName;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 业务域
     */
    private String businessDomain;

    /**
     * 状态
     */
    private String status;

    /**
     * 是否核心事件
     */
    private Boolean isCore;
}

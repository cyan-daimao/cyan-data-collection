package com.cyan.datacollection.adapter.debug.controller.request;

import lombok.Data;

/**
 * Debug 事件样本分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class DebugEventPageQuery {

    /**
     * 页码
     */
    private long pageNum = 1;

    /**
     * 页大小
     */
    private long pageSize = 20;

    /**
     * Debug Token
     */
    private String debugToken;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 环境
     */
    private String environment;
}

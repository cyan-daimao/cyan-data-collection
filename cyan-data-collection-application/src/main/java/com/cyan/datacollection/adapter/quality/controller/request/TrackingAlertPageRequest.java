package com.cyan.datacollection.adapter.quality.controller.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 告警分页请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingAlertPageRequest {

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 告警类型
     */
    private String alertType;

    /**
     * 告警级别
     */
    private String alertLevel;

    /**
     * 状态
     */
    private String status;

    /**
     * 页码
     */
    @JsonAlias({"pageNum", "pageNo"})
    private long current = 1;

    /**
     * 每页大小
     */
    @JsonAlias("pageSize")
    private long size = 10;
}

package com.cyan.datacollection.adapter.acceptance.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 创建验收任务请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingAcceptanceTaskCreateRequest {

    /**
     * 方案ID
     */
    private String planId;

    /**
     * Debug Token
     */
    private String debugToken;

    /**
     * 环境
     */
    private String environment;
}

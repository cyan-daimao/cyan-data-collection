package com.cyan.datacollection.adapter.app.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 接入应用集成示例DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingAppIntegrationDTO {

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 上报地址
     */
    private String reportUrl;

    /**
     * JavaScript SDK 示例
     */
    private String jsSdkCode;

    /**
     * Java SDK 示例
     */
    private String javaSdkCode;
}

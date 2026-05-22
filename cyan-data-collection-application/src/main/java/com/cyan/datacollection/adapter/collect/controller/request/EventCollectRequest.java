package com.cyan.datacollection.adapter.collect.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 事件上报请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EventCollectRequest {

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * Debug Token
     */
    private String debugToken;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 事件发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime eventTime;

    /**
     * 端类型
     */
    private String terminalType;

    /**
     * 环境
     */
    private String environment;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 匿名ID
     */
    private String anonymousId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * SDK版本
     */
    private String sdkVersion;

    /**
     * 应用版本
     */
    private String appVersion;

    /**
     * 页面编码
     */
    private String pageCode;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 属性
     */
    private Map<String, Object> properties;
}

package com.cyan.datacollection.adapter.debug.controller.dto;

import com.cyan.datacollection.enums.Environment;
import com.cyan.datacollection.enums.TerminalType;
import com.cyan.datacollection.enums.ValidateStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Debug 事件样本DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DebugEventSampleDTO {

    /**
     * 主键
     */
    private String id;

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
     * 服务端接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime ingestionTime;

    /**
     * 端类型
     */
    private TerminalType terminalType;

    /**
     * 环境
     */
    private Environment environment;

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
     * 公共上下文JSON
     */
    private String common;

    /**
     * 行为信息JSON
     */
    private String action;

    /**
     * 业务字段JSON
     */
    private String business;

    /**
     * 额外扩展JSON
     */
    private String extra;

    /**
     * 原始JSON payload
     */
    private String payload;

    /**
     * 校验状态
     */
    private ValidateStatus validateStatus;

    /**
     * 校验错误列表
     */
    private List<String> validateErrors;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
}

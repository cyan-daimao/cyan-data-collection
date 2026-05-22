package com.cyan.datacollection.adapter.debug.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 创建 Debug 会话请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DebugSessionCreateRequest {

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 匿名ID
     */
    private String anonymousId;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 环境
     */
    private String environment;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expiredAt;
}

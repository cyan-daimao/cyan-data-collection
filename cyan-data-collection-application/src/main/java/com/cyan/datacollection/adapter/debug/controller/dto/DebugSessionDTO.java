package com.cyan.datacollection.adapter.debug.controller.dto;

import com.cyan.datacollection.enums.DebugSessionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Debug 会话DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DebugSessionDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * Debug Token
     */
    private String debugToken;

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

    /**
     * 状态
     */
    private DebugSessionStatus status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;
}

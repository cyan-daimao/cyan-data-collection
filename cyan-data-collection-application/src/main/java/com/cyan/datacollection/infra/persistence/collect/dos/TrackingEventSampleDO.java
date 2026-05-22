package com.cyan.datacollection.infra.persistence.collect.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyan.datacollection.enums.Environment;
import com.cyan.datacollection.enums.TerminalType;
import com.cyan.datacollection.enums.ValidateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 上报事件样本表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("tracking_event_sample")
public class TrackingEventSampleDO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 应用编码
     */
    @TableField("app_code")
    private String appCode;

    /**
     * Debug令牌
     */
    @TableField("debug_token")
    private String debugToken;

    /**
     * 事件编码
     */
    @TableField("event_code")
    private String eventCode;

    /**
     * 事件时间
     */
    @TableField("event_time")
    private LocalDateTime eventTime;

    /**
     * 采集时间
     */
    @TableField("ingestion_time")
    private LocalDateTime ingestionTime;

    /**
     * 终端类型
     */
    @TableField("terminal_type")
    private TerminalType terminalType;

    /**
     * 环境
     */
    @TableField("environment")
    private Environment environment;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 匿名ID
     */
    @TableField("anonymous_id")
    private String anonymousId;

    /**
     * 会话ID
     */
    @TableField("session_id")
    private String sessionId;

    /**
     * 设备ID
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * SDK版本
     */
    @TableField("sdk_version")
    private String sdkVersion;

    /**
     * 应用版本
     */
    @TableField("app_version")
    private String appVersion;

    /**
     * 页面编码
     */
    @TableField("page_code")
    private String pageCode;

    /**
     * 请求ID
     */
    @TableField("request_id")
    private String requestId;

    /**
     * 上报数据载荷
     */
    @TableField("payload")
    private String payload;

    /**
     * 校验状态
     */
    @TableField("validate_status")
    private ValidateStatus validateStatus;

    /**
     * 校验错误信息
     */
    @TableField("validate_errors")
    private String validateErrors;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 删除时间
     */
    @TableField("deleted_at")
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;
}

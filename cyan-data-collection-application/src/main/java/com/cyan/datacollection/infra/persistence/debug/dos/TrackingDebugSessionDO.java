package com.cyan.datacollection.infra.persistence.debug.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyan.datacollection.enums.DebugSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Debug 会话表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("tracking_debug_session")
public class TrackingDebugSessionDO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * Debug令牌
     */
    @TableField("debug_token")
    private String debugToken;

    /**
     * 应用编码
     */
    @TableField("app_code")
    private String appCode;

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
     * 设备ID
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * 环境
     */
    @TableField("environment")
    private String environment;

    /**
     * 过期时间
     */
    @TableField("expired_at")
    private LocalDateTime expiredAt;

    /**
     * 状态
     */
    @TableField("status")
    private DebugSessionStatus status;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createBy;

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

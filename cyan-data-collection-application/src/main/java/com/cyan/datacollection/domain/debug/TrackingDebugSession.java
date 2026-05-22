package com.cyan.datacollection.domain.debug;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.debug.repository.TrackingDebugSessionRepository;
import com.cyan.datacollection.enums.DebugSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Debug 会话
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingDebugSession {

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
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除时间
     */
    private LocalDateTime deletedAt;

    public TrackingDebugSession save(TrackingDebugSessionRepository repository) {
        if (this.debugToken == null || this.debugToken.isEmpty()) {
            this.debugToken = "debug_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        }
        if (this.expiredAt == null) {
            this.expiredAt = LocalDateTime.now().plusHours(2);
        }
        this.status = DebugSessionStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    public void expireIfNeeded() {
        if (this.status == DebugSessionStatus.ACTIVE && this.expiredAt != null && LocalDateTime.now().isAfter(this.expiredAt)) {
            this.status = DebugSessionStatus.EXPIRED;
        }
    }
}

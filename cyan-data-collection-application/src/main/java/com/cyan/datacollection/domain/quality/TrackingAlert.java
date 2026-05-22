package com.cyan.datacollection.domain.quality;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.quality.repository.TrackingAlertRepository;
import com.cyan.datacollection.enums.AlertLevel;
import com.cyan.datacollection.enums.AlertStatus;
import com.cyan.datacollection.enums.AlertType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 埋点质量告警
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingAlert {

    /**
     * 主键
     */
    private String id;

    /**
     * 告警类型
     */
    private AlertType alertType;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 告警级别
     */
    private AlertLevel alertLevel;

    /**
     * 告警信息
     */
    private String alertMessage;

    /**
     * 状态
     */
    private AlertStatus status;

    /**
     * 触发时间
     */
    private LocalDateTime triggeredAt;

    /**
     * 关闭时间
     */
    private LocalDateTime closedAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除时间
     */
    private LocalDateTime deletedAt;

    /**
     * 校验
     */
    private void validate() {
        Assert.notNull(this.alertType, new SilentException("告警类型不能为空"));
        Assert.notBlank(this.appCode, new SilentException("应用编码不能为空"));
        Assert.notBlank(this.eventCode, new SilentException("事件编码不能为空"));
    }

    /**
     * 保存
     */
    public TrackingAlert save(TrackingAlertRepository repository) {
        validate();
        this.status = AlertStatus.OPEN;
        this.triggeredAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    /**
     * 关闭告警
     */
    public TrackingAlert close(TrackingAlertRepository repository) {
        Assert.notBlank(this.id, new SilentException("告警ID不能为空"));
        if (this.status == AlertStatus.CLOSED) {
            throw new SilentException("告警已关闭");
        }
        this.status = AlertStatus.CLOSED;
        this.closedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }
}

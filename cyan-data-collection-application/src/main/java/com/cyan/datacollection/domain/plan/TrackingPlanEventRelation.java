package com.cyan.datacollection.domain.plan;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 方案事件关系（埋点方案中的事件配置）
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingPlanEventRelation {

    /**
     * 主键
     */
    private String id;

    /**
     * 方案ID
     */
    private String planId;

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 在该方案中是否必填
     */
    private Boolean isRequired;

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
     * 校验并保存方案事件关系
     */
    public TrackingPlanEventRelation save() {
        Assert.notBlank(this.planId, new SilentException("方案ID不能为空"));
        Assert.notBlank(this.eventId, new SilentException("事件ID不能为空"));
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    /**
     * 校验并更新方案事件关系
     */
    public TrackingPlanEventRelation update() {
        Assert.notBlank(this.id, new SilentException("方案事件关系ID不能为空"));
        Assert.notBlank(this.planId, new SilentException("方案ID不能为空"));
        Assert.notBlank(this.eventId, new SilentException("事件ID不能为空"));
        this.updatedAt = LocalDateTime.now();
        return this;
    }
}
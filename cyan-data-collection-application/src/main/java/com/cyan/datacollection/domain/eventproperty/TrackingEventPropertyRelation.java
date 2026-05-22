package com.cyan.datacollection.domain.eventproperty;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 事件属性关系
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingEventPropertyRelation {

    /**
     * 主键
     */
    private String id;

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 属性ID
     */
    private String propertyId;

    /**
     * 在该事件中是否必填
     */
    private Boolean isRequired;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 样例值
     */
    private String sampleValue;

    /**
     * 在该事件中的说明
     */
    private String description;

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
     * 校验并保存事件属性关系
     */
    public TrackingEventPropertyRelation save() {
        Assert.notBlank(this.eventId, new SilentException("事件ID不能为空"));
        Assert.notBlank(this.propertyId, new SilentException("属性ID不能为空"));
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    /**
     * 校验并更新事件属性关系
     */
    public TrackingEventPropertyRelation update() {
        Assert.notBlank(this.id, new SilentException("事件属性关系ID不能为空"));
        Assert.notBlank(this.eventId, new SilentException("事件ID不能为空"));
        Assert.notBlank(this.propertyId, new SilentException("属性ID不能为空"));
        this.updatedAt = LocalDateTime.now();
        return this;
    }
}
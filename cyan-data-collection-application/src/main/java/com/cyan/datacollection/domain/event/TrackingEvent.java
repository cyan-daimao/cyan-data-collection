package com.cyan.datacollection.domain.event;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.event.repository.TrackingEventRepository;
import com.cyan.datacollection.enums.EventStatus;
import com.cyan.datacollection.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 事件定义
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingEvent {

    /**
     * 主键
     */
    private String id;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件类型
     */
    private EventType eventType;

    /**
     * 业务域
     */
    private String businessDomain;

    /**
     * 业务描述
     */
    private String description;

    /**
     * 触发时机
     */
    private String triggerTiming;

    /**
     * 支持端类型
     */
    private List<String> terminalTypes;

    /**
     * 负责人
     */
    private String owner;

    /**
     * 是否核心事件
     */
    private Boolean isCore;

    /**
     * 状态
     */
    private EventStatus status;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

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
        Assert.notBlank(this.eventCode, new SilentException("事件编码不能为空"));
        Assert.notBlank(this.appCode, new SilentException("应用编码不能为空"));
        Assert.notBlank(this.eventName, new SilentException("事件名称不能为空"));
        Assert.notNull(this.eventType, new SilentException("事件类型不能为空"));
        Assert.notBlank(this.businessDomain, new SilentException("业务域不能为空"));
        Assert.isTrue(this.eventCode.startsWith(this.businessDomain + "_"),
                new SilentException("事件编码必须以业务域编码加下划线开头"));
        if (Boolean.TRUE.equals(this.isCore)) {
            Assert.notBlank(this.owner, new SilentException("核心事件必须配置负责人"));
            Assert.notBlank(this.triggerTiming, new SilentException("核心事件必须配置触发时机"));
        }
    }

    /**
     * 保存
     */
    public TrackingEvent save(TrackingEventRepository repository) {
        validate();
        this.status = EventStatus.DRAFT;
        this.version = 1;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    /**
     * 更新
     */
    public TrackingEvent update(TrackingEventRepository repository) {
        validate();
        Assert.notBlank(this.id, new SilentException("事件ID不能为空"));
        Assert.isTrue(this.status == EventStatus.DRAFT, new SilentException("只有草稿状态可编辑"));
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 发布
     */
    public TrackingEvent publish(TrackingEventRepository repository) {
        Assert.notNull(this.status, new SilentException("事件状态异常"));
        if (this.status == EventStatus.PUBLISHED) {
            throw new SilentException("事件已发布");
        }
        if (this.status == EventStatus.DEPRECATED) {
            throw new SilentException("已废弃事件不可发布");
        }
        this.status = EventStatus.PUBLISHED;
        this.version = (this.version == null ? 1 : this.version) + 1;
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 废弃
     */
    public TrackingEvent deprecate(TrackingEventRepository repository) {
        Assert.notNull(this.status, new SilentException("事件状态异常"));
        if (this.status == EventStatus.DEPRECATED) {
            throw new SilentException("事件已废弃");
        }
        this.status = EventStatus.DEPRECATED;
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 删除
     */
    public void delete(TrackingEventRepository repository) {
        Assert.notBlank(this.id, new SilentException("事件ID不能为空"));
        repository.deleteById(this.id);
    }
}

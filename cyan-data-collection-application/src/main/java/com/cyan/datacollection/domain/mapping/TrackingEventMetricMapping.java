package com.cyan.datacollection.domain.mapping;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.mapping.repository.TrackingEventMetricMappingRepository;
import com.cyan.datacollection.enums.SyncStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 采集事件指标映射
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingEventMetricMapping {

    /**
     * 主键
     */
    private String id;

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 指标ID
     */
    private String metricId;

    /**
     * 指标编码
     */
    private String metricCode;

    /**
     * 同步状态
     */
    private SyncStatus syncStatus;

    /**
     * 错误信息
     */
    private String errorMessage;

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
     * 保存
     */
    public TrackingEventMetricMapping save(TrackingEventMetricMappingRepository repository) {
        validate();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    /**
     * 更新
     */
    public TrackingEventMetricMapping update(TrackingEventMetricMappingRepository repository) {
        validate();
        Assert.notBlank(this.id, new SilentException("事件指标映射ID不能为空"));
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 标记成功
     */
    public void markSuccess(String metricId, String metricCode, String updatedBy) {
        this.metricId = metricId;
        this.metricCode = metricCode;
        this.syncStatus = SyncStatus.SUCCESS;
        this.errorMessage = null;
        this.updateBy = updatedBy;
    }

    /**
     * 标记失败
     */
    public void markFailed(String errorMessage, String updatedBy) {
        this.syncStatus = SyncStatus.FAILED;
        this.errorMessage = errorMessage;
        this.updateBy = updatedBy;
    }

    private void validate() {
        Assert.notBlank(this.eventId, new SilentException("事件ID不能为空"));
        Assert.notBlank(this.eventCode, new SilentException("事件编码不能为空"));
        Assert.notBlank(this.metricCode, new SilentException("指标编码不能为空"));
        Assert.notNull(this.syncStatus, new SilentException("同步状态不能为空"));
    }
}

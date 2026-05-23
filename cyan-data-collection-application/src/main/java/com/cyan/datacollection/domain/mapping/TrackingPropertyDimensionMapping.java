package com.cyan.datacollection.domain.mapping;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.mapping.repository.TrackingPropertyDimensionMappingRepository;
import com.cyan.datacollection.enums.SyncStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 采集属性维度映射
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingPropertyDimensionMapping {

    /**
     * 主键
     */
    private String id;

    /**
     * 属性ID
     */
    private String propertyId;

    /**
     * 属性编码
     */
    private String propertyCode;

    /**
     * 维度ID
     */
    private String dimId;

    /**
     * 维度编码
     */
    private String dimCode;

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
    public TrackingPropertyDimensionMapping save(TrackingPropertyDimensionMappingRepository repository) {
        validate();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    /**
     * 更新
     */
    public TrackingPropertyDimensionMapping update(TrackingPropertyDimensionMappingRepository repository) {
        validate();
        Assert.notBlank(this.id, new SilentException("属性维度映射ID不能为空"));
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 标记成功
     */
    public void markSuccess(String dimId, String dimCode, String updatedBy) {
        this.dimId = dimId;
        this.dimCode = dimCode;
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
        Assert.notBlank(this.propertyId, new SilentException("属性ID不能为空"));
        Assert.notBlank(this.propertyCode, new SilentException("属性编码不能为空"));
        Assert.notBlank(this.dimCode, new SilentException("维度编码不能为空"));
        Assert.notNull(this.syncStatus, new SilentException("同步状态不能为空"));
    }
}

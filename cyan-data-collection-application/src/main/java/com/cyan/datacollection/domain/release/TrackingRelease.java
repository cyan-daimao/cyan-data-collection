package com.cyan.datacollection.domain.release;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.release.repository.TrackingReleaseRepository;
import com.cyan.datacollection.enums.ReleaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 埋点发布版本
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingRelease {

    /**
     * 主键
     */
    private String id;

    /**
     * 发布编号
     */
    private String releaseCode;

    /**
     * 方案ID
     */
    private String planId;

    /**
     * 发布版本
     */
    private Integer version;

    /**
     * 状态
     */
    private ReleaseStatus status;

    /**
     * 变更摘要JSON
     */
    private String diffSummary;

    /**
     * 发布时间
     */
    private LocalDateTime publishedAt;

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
        Assert.notBlank(this.planId, new SilentException("方案ID不能为空"));
        Assert.notNull(this.version, new SilentException("版本号不能为空"));
    }

    /**
     * 保存
     */
    public TrackingRelease save(TrackingReleaseRepository repository) {
        validate();
        this.status = ReleaseStatus.DRAFT;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    /**
     * 提交发布
     */
    public TrackingRelease submit(TrackingReleaseRepository repository) {
        Assert.notBlank(this.id, new SilentException("发布ID不能为空"));
        if (this.status != ReleaseStatus.DRAFT) {
            throw new SilentException("只有草稿状态可提交");
        }
        this.status = ReleaseStatus.SUBMITTED;
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 发布上线
     */
    public TrackingRelease publish(TrackingReleaseRepository repository) {
        Assert.notBlank(this.id, new SilentException("发布ID不能为空"));
        if (this.status != ReleaseStatus.SUBMITTED && this.status != ReleaseStatus.DRAFT) {
            throw new SilentException("当前状态不可发布");
        }
        this.status = ReleaseStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 取消发布
     */
    public TrackingRelease cancel(TrackingReleaseRepository repository) {
        Assert.notBlank(this.id, new SilentException("发布ID不能为空"));
        if (this.status == ReleaseStatus.PUBLISHED) {
            throw new SilentException("已发布状态不可取消");
        }
        this.status = ReleaseStatus.CANCELED;
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }
}

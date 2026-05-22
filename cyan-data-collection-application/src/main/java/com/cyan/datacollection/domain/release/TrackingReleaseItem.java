package com.cyan.datacollection.domain.release;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.release.repository.TrackingReleaseItemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 埋点发布版本明细
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingReleaseItem {

    /**
     * 主键
     */
    private String id;

    /**
     * 发布ID
     */
    private String releaseId;

    /**
     * 对象类型: PLAN, EVENT, PROPERTY
     */
    private String itemType;

    /**
     * 对象ID
     */
    private String itemId;

    /**
     * 对象编码
     */
    private String itemCode;

    /**
     * 变更类型: ADD, UPDATE, DELETE
     */
    private String changeType;

    /**
     * 发布快照JSON
     */
    private String snapshot;

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
     * 校验并保存发布明细
     */
    public TrackingReleaseItem save(TrackingReleaseItemRepository repository) {
        Assert.notBlank(this.releaseId, new SilentException("发布ID不能为空"));
        Assert.notBlank(this.itemType, new SilentException("对象类型不能为空"));
        Assert.notBlank(this.itemId, new SilentException("对象ID不能为空"));
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }
}

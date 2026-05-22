package com.cyan.datacollection.domain.plan;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.plan.repository.TrackingPlanRepository;
import com.cyan.datacollection.enums.PlanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 埋点方案
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingPlan {

    /**
     * 主键
     */
    private String id;

    /**
     * 方案编号
     */
    private String planCode;

    /**
     * 方案名称
     */
    private String planName;

    /**
     * 关联需求ID
     */
    private String demandId;

    /**
     * 关联需求编号
     */
    private String demandCode;

    /**
     * 方案版本
     */
    private Integer version;

    /**
     * 方案描述
     */
    private String description;

    /**
     * 状态
     */
    private PlanStatus status;

    /**
     * 评审人
     */
    private String reviewer;

    /**
     * 已发布版本ID
     */
    private String publishedVersionId;

    /**
     * 方案内事件列表（非持久化，运行时组装）
     */
    private List<PlanEvent> events;

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
        Assert.notBlank(this.planName, new SilentException("方案名称不能为空"));
    }

    /**
     * 保存
     */
    public TrackingPlan save(TrackingPlanRepository repository) {
        validate();
        this.status = PlanStatus.DRAFT;
        this.version = 1;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    /**
     * 更新
     */
    public TrackingPlan update(TrackingPlanRepository repository) {
        validate();
        Assert.notBlank(this.id, new SilentException("方案ID不能为空"));
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 删除
     */
    public void delete(TrackingPlanRepository repository) {
        Assert.notBlank(this.id, new SilentException("方案ID不能为空"));
        repository.deleteById(this.id);
    }

    /**
     * 提交评审
     */
    public TrackingPlan submitReview(TrackingPlanRepository repository) {
        Assert.notBlank(this.id, new SilentException("方案ID不能为空"));
        if (this.status != PlanStatus.DRAFT) {
            throw new SilentException("只有草稿状态可提交评审");
        }
        this.status = PlanStatus.REVIEWING;
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 评审通过
     */
    public TrackingPlan approve(TrackingPlanRepository repository) {
        Assert.notBlank(this.id, new SilentException("方案ID不能为空"));
        if (this.status != PlanStatus.REVIEWING) {
            throw new SilentException("只有评审中状态可通过评审");
        }
        this.status = PlanStatus.DEVELOPING;
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 评审驳回
     */
    public TrackingPlan reject(TrackingPlanRepository repository) {
        Assert.notBlank(this.id, new SilentException("方案ID不能为空"));
        if (this.status != PlanStatus.REVIEWING) {
            throw new SilentException("只有评审中状态可驳回");
        }
        this.status = PlanStatus.DRAFT;
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 发布上线
     */
    public TrackingPlan publish(TrackingPlanRepository repository, String releaseId) {
        Assert.notBlank(this.id, new SilentException("方案ID不能为空"));
        if (this.status == PlanStatus.PUBLISHED) {
            throw new SilentException("方案已发布");
        }
        this.status = PlanStatus.PUBLISHED;
        this.publishedVersionId = releaseId;
        this.version = (this.version == null ? 1 : this.version) + 1;
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 方案内事件引用
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class PlanEvent {

        /**
         * 事件ID
         */
        private String eventId;

        /**
         * 事件编码
         */
        private String eventCode;

        /**
         * 事件名称
         */
        private String eventName;

        /**
         * 事件类型
         */
        private String eventType;

        /**
         * 在该方案中是否必填
         */
        private Boolean isRequired;
    }
}

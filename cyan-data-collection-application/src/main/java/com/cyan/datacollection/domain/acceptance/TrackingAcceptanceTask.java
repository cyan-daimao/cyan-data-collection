package com.cyan.datacollection.domain.acceptance;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.acceptance.repository.TrackingAcceptanceTaskRepository;
import com.cyan.datacollection.enums.AcceptanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 埋点验收任务
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingAcceptanceTask {

    /**
     * 主键
     */
    private String id;

    /**
     * 验收任务编号
     */
    private String taskCode;

    /**
     * 方案ID
     */
    private String planId;

    /**
     * Debug Token
     */
    private String debugToken;

    /**
     * 环境
     */
    private String environment;

    /**
     * 状态
     */
    private AcceptanceStatus status;

    /**
     * 事件覆盖率
     */
    private BigDecimal eventCoverageRate;

    /**
     * 必填属性完整率
     */
    private BigDecimal requiredPropertyCompleteRate;

    /**
     * 类型正确率
     */
    private BigDecimal typeValidRate;

    /**
     * 结果摘要JSON
     */
    private String resultSummary;

    /**
     * 验收结果列表（非持久化，运行时组装）
     */
    private List<TrackingAcceptanceResult> results;

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
        Assert.notBlank(this.debugToken, new SilentException("Debug Token不能为空"));
    }

    /**
     * 保存
     */
    public TrackingAcceptanceTask save(TrackingAcceptanceTaskRepository repository) {
        validate();
        this.status = AcceptanceStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    /**
     * 更新
     */
    public TrackingAcceptanceTask update(TrackingAcceptanceTaskRepository repository) {
        validate();
        Assert.notBlank(this.id, new SilentException("验收任务ID不能为空"));
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 删除
     */
    public void delete(TrackingAcceptanceTaskRepository repository) {
        Assert.notBlank(this.id, new SilentException("验收任务ID不能为空"));
        repository.deleteById(this.id);
    }

    /**
     * 验收通过
     */
    public TrackingAcceptanceTask approve(TrackingAcceptanceTaskRepository repository) {
        Assert.notBlank(this.id, new SilentException("验收任务ID不能为空"));
        if (this.status != AcceptanceStatus.PASS && this.status != AcceptanceStatus.FAIL) {
            throw new SilentException("只有执行完成状态才可通过验收");
        }
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    /**
     * 验收驳回
     */
    public TrackingAcceptanceTask reject(TrackingAcceptanceTaskRepository repository) {
        Assert.notBlank(this.id, new SilentException("验收任务ID不能为空"));
        if (this.status != AcceptanceStatus.PASS && this.status != AcceptanceStatus.FAIL) {
            throw new SilentException("只有执行完成状态才可驳回验收");
        }
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }
}

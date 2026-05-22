package com.cyan.datacollection.domain.acceptance;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.acceptance.repository.TrackingAcceptanceResultRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 埋点验收结果
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingAcceptanceResult {

    /**
     * 主键
     */
    private String id;

    /**
     * 验收任务ID
     */
    private String taskId;

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 状态: PASS, FAIL
     */
    private String status;

    /**
     * 错误项列表
     */
    private List<String> errorItems;

    /**
     * 命中的样本ID列表
     */
    private List<String> sampleIds;

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
     * 校验并保存
     */
    public TrackingAcceptanceResult save(TrackingAcceptanceResultRepository repository) {
        Assert.notBlank(this.taskId, new SilentException("验收任务ID不能为空"));
        Assert.notBlank(this.eventId, new SilentException("事件ID不能为空"));
        Assert.notBlank(this.eventCode, new SilentException("事件编码不能为空"));
        Assert.notBlank(this.status, new SilentException("状态不能为空"));
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    /**
     * 批量保存
     */
    public static List<TrackingAcceptanceResult> saveBatch(TrackingAcceptanceResultRepository repository, List<TrackingAcceptanceResult> results) {
        results.forEach(r -> {
            Assert.notBlank(r.getTaskId(), new SilentException("验收任务ID不能为空"));
            Assert.notBlank(r.getEventId(), new SilentException("事件ID不能为空"));
            Assert.notBlank(r.getEventCode(), new SilentException("事件编码不能为空"));
            Assert.notBlank(r.getStatus(), new SilentException("状态不能为空"));
            r.setCreatedAt(LocalDateTime.now());
            r.setUpdatedAt(LocalDateTime.now());
        });
        return repository.saveBatch(results);
    }
}

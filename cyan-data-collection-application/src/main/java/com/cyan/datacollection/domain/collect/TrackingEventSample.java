package com.cyan.datacollection.domain.collect;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.collect.repository.TrackingEventSampleRepository;
import com.cyan.datacollection.enums.ValidateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 事件样本
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingEventSample {

    /**
     * 主键
     */
    private String id;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 事件发生时间
     */
    private LocalDateTime eventTime;

    /**
     * 服务端接收时间
     */
    private LocalDateTime ingestionTime;

    /**
     * Debug Token
     */
    private String debugToken;

    /**
     * 公共上下文JSON
     */
    private String common;

    /**
     * 行为信息JSON
     */
    private String action;

    /**
     * 业务字段JSON
     */
    private String business;

    /**
     * 额外扩展JSON
     */
    private String extra;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 原始JSON payload
     */
    private String payload;

    /**
     * 校验状态
     */
    private ValidateStatus validateStatus;

    /**
     * 校验错误列表
     */
    private List<String> validateErrors;

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
     * 校验事件样本数据并保存
     */
    public TrackingEventSample save(TrackingEventSampleRepository repository) {
        Assert.notBlank(this.appCode, new SilentException("应用编码不能为空"));
        Assert.notBlank(this.eventCode, new SilentException("事件编码不能为空"));
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    /**
     * 批量保存事件样本
     */
    public static List<TrackingEventSample> saveBatch(TrackingEventSampleRepository repository, List<TrackingEventSample> samples) {
        samples.forEach(s -> {
            Assert.notBlank(s.getAppCode(), new SilentException("应用编码不能为空"));
            Assert.notBlank(s.getEventCode(), new SilentException("事件编码不能为空"));
            s.setCreatedAt(LocalDateTime.now());
            s.setUpdatedAt(LocalDateTime.now());
        });
        return repository.saveBatch(samples);
    }

    /**
     * 标记校验通过
     */
    public void markValidated() {
        this.validateStatus = ValidateStatus.PASS;
        this.validateErrors = null;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 标记校验失败
     */
    public void markValidationFailed(List<String> errors) {
        this.validateStatus = ValidateStatus.FAIL;
        this.validateErrors = errors;
        this.updatedAt = LocalDateTime.now();
    }
}

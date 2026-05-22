package com.cyan.datacollection.domain.quality;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 埋点质量指标
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingQualityMetric {

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
     * 环境
     */
    private String environment;

    /**
     * 统计时间
     */
    private LocalDateTime metricTime;

    /**
     * 统计粒度
     */
    private String metricGranularity;

    /**
     * 总上报量
     */
    private Long totalCount;

    /**
     * 通过量
     */
    private Long passCount;

    /**
     * 警告量
     */
    private Long warnCount;

    /**
     * 失败量
     */
    private Long failCount;

    /**
     * 通过率
     */
    private BigDecimal passRate;

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
}

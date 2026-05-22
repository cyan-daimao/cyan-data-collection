package com.cyan.datacollection.infra.persistence.quality.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 埋点质量指标表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("tracking_quality_metric")
public class TrackingQualityMetricDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 应用编码
     */
    @TableField("app_code")
    private String appCode;

    /**
     * 事件编码
     */
    @TableField("event_code")
    private String eventCode;

    /**
     * 环境
     */
    @TableField("environment")
    private String environment;

    /**
     * 统计时间
     */
    @TableField("metric_time")
    private LocalDateTime metricTime;

    /**
     * 统计粒度
     */
    @TableField("metric_granularity")
    private String metricGranularity;

    /**
     * 总上报量
     */
    @TableField("total_count")
    private Long totalCount;

    /**
     * 通过量
     */
    @TableField("pass_count")
    private Long passCount;

    /**
     * 警告量
     */
    @TableField("warn_count")
    private Long warnCount;

    /**
     * 失败量
     */
    @TableField("fail_count")
    private Long failCount;

    /**
     * 通过率
     */
    @TableField("pass_rate")
    private BigDecimal passRate;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除时间
     */
    @TableField("deleted_at")
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;
}

package com.cyan.datacollection.infra.persistence.acceptance.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyan.datacollection.enums.AcceptanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 埋点验收任务表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("tracking_acceptance_task")
public class TrackingAcceptanceTaskDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 验收任务编号
     */
    @TableField("task_code")
    private String taskCode;

    /**
     * 方案ID
     */
    @TableField("plan_id")
    private Long planId;

    /**
     * Debug Token
     */
    @TableField("debug_token")
    private String debugToken;

    /**
     * 环境
     */
    @TableField("environment")
    private String environment;

    /**
     * 状态
     */
    @TableField("status")
    private AcceptanceStatus status;

    /**
     * 事件覆盖率
     */
    @TableField("event_coverage_rate")
    private BigDecimal eventCoverageRate;

    /**
     * 必填属性完整率
     */
    @TableField("required_property_complete_rate")
    private BigDecimal requiredPropertyCompleteRate;

    /**
     * 类型正确率
     */
    @TableField("type_valid_rate")
    private BigDecimal typeValidRate;

    /**
     * 结果摘要JSON
     */
    @TableField("result_summary")
    private String resultSummary;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private String updateBy;

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
     * 逻辑删除
     */
    @TableField("deleted_at")
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;
}

package com.cyan.datacollection.infra.persistence.acceptance.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 埋点验收结果表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("tracking_acceptance_result")
public class TrackingAcceptanceResultDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 验收任务ID
     */
    @TableField("task_id")
    private Long taskId;

    /**
     * 事件ID
     */
    @TableField("event_id")
    private Long eventId;

    /**
     * 事件编码
     */
    @TableField("event_code")
    private String eventCode;

    /**
     * 状态: PASS, FAIL
     */
    @TableField("status")
    private String status;

    /**
     * 错误项JSON
     */
    @TableField("error_items")
    private String errorItems;

    /**
     * 命中的样本ID JSON
     */
    @TableField("sample_ids")
    private String sampleIds;

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

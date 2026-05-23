package com.cyan.datacollection.infra.persistence.mapping.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyan.datacollection.enums.SyncStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 采集事件指标映射DO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
@TableName("tracking_event_metric_mapping")
public class TrackingEventMetricMappingDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

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
     * 指标ID
     */
    @TableField("metric_id")
    private String metricId;

    /**
     * 指标编码
     */
    @TableField("metric_code")
    private String metricCode;

    /**
     * 同步状态
     */
    @TableField("sync_status")
    private SyncStatus syncStatus;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

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
     * 逻辑删除时间
     */
    @TableField("deleted_at")
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;
}

package com.cyan.datacollection.infra.persistence.release.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyan.datacollection.enums.ReleaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 埋点发布版本表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("tracking_release")
public class TrackingReleaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发布编号
     */
    @TableField("release_code")
    private String releaseCode;

    /**
     * 方案ID
     */
    @TableField("plan_id")
    private Long planId;

    /**
     * 发布版本
     */
    @TableField("version")
    private Integer version;

    /**
     * 状态
     */
    @TableField("status")
    private ReleaseStatus status;

    /**
     * 变更摘要JSON
     */
    @TableField("diff_summary")
    private String diffSummary;

    /**
     * 发布时间
     */
    @TableField("published_at")
    private LocalDateTime publishedAt;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private String updatedBy;

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

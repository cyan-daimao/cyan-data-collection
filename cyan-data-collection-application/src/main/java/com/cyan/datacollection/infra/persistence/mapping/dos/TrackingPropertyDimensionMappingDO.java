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
 * 采集属性维度映射DO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
@TableName("tracking_property_dimension_mapping")
public class TrackingPropertyDimensionMappingDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 属性ID
     */
    @TableField("property_id")
    private Long propertyId;

    /**
     * 属性编码
     */
    @TableField("property_code")
    private String propertyCode;

    /**
     * 维度ID
     */
    @TableField("dim_id")
    private String dimId;

    /**
     * 维度编码
     */
    @TableField("dim_code")
    private String dimCode;

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

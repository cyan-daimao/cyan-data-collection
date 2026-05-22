package com.cyan.datacollection.infra.persistence.app.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyan.datacollection.enums.AppStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 接入应用表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("tracking_app")
public class TrackingAppDO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 应用编码
     */
    @TableField("app_code")
    private String appCode;

    /**
     * 应用名称
     */
    @TableField("app_name")
    private String appName;

    /**
     * 应用类型
     */
    @TableField("app_type")
    private String appType;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 密钥
     */
    @TableField("secret_key")
    private String secretKey;

    /**
     * 上报地址
     */
    @TableField("report_url")
    private String reportUrl;

    /**
     * 状态
     */
    @TableField("status")
    private AppStatus status;

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
     * 删除时间
     */
    @TableField("deleted_at")
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;
}

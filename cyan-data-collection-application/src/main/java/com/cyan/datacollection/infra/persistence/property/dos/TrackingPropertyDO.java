package com.cyan.datacollection.infra.persistence.property.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyan.datacollection.enums.DataType;
import com.cyan.datacollection.enums.PropertyStatus;
import com.cyan.datacollection.enums.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 属性定义表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("tracking_property")
public class TrackingPropertyDO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 属性编码
     */
    @TableField("property_code")
    private String propertyCode;

    /**
     * 属性名称
     */
    @TableField("property_name")
    private String propertyName;

    /**
     * 属性类型
     */
    @TableField("property_type")
    private PropertyType propertyType;

    /**
     * 数据类型
     */
    @TableField("data_type")
    private DataType dataType;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 是否必填
     */
    @TableField("is_required")
    private Boolean isRequired;

    /**
     * 是否敏感
     */
    @TableField("is_sensitive")
    private Boolean isSensitive;

    /**
     * 安全等级
     */
    @TableField("security_level")
    private String securityLevel;

    /**
     * 枚举值
     */
    @TableField("enum_values")
    private String enumValues;

    /**
     * 最大长度
     */
    @TableField("max_length")
    private Integer maxLength;

    /**
     * 校验规则
     */
    @TableField("validation_rule")
    private String validationRule;

    /**
     * 标准编码
     */
    @TableField("standard_code")
    private String standardCode;

    /**
     * 状态
     */
    @TableField("status")
    private PropertyStatus status;

    /**
     * 版本
     */
    @TableField("version")
    private Integer version;

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

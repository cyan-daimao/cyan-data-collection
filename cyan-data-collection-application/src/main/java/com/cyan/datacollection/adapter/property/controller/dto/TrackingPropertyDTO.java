package com.cyan.datacollection.adapter.property.controller.dto;

import com.cyan.datacollection.enums.DataType;
import com.cyan.datacollection.enums.PropertyStatus;
import com.cyan.datacollection.enums.PropertyType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 属性定义DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingPropertyDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 属性编码
     */
    private String propertyCode;

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 属性类型
     */
    private PropertyType propertyType;

    /**
     * 数据类型
     */
    private DataType dataType;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否默认必填
     */
    private Boolean isRequired;

    /**
     * 是否敏感
     */
    private Boolean isSensitive;

    /**
     * 安全等级
     */
    private String securityLevel;

    /**
     * 枚举值
     */
    private List<String> enumValues;

    /**
     * 最大长度
     */
    private Integer maxLength;

    /**
     * 校验规则
     */
    private String validationRule;

    /**
     * 关联数据标准编码
     */
    private String standardCode;

    /**
     * 状态
     */
    private PropertyStatus status;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;
}

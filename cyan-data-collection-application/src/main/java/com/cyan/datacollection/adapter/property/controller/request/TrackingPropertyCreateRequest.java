package com.cyan.datacollection.adapter.property.controller.request;

import com.cyan.datacollection.enums.DataType;
import com.cyan.datacollection.enums.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 创建属性请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingPropertyCreateRequest {

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
}

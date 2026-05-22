package com.cyan.datacollection.adapter.event.controller.dto;

import com.cyan.datacollection.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 事件属性DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EventPropertyDTO {

    /**
     * 事件属性关系ID
     */
    private String id;

    /**
     * 属性ID
     */
    private String propertyId;

    /**
     * 属性编码
     */
    private String propertyCode;

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 数据类型
     */
    private DataType dataType;

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
     * 是否敏感
     */
    private Boolean isSensitive;

    /**
     * 在该事件中是否必填
     */
    private Boolean isRequired;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 样例值
     */
    private String sampleValue;

    /**
     * 说明
     */
    private String description;
}

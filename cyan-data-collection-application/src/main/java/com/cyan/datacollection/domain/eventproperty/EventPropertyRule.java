package com.cyan.datacollection.domain.eventproperty;

import com.cyan.datacollection.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 事件属性规则 — 只读查询模型
 * 用于上报校验时获取属性约束规则
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EventPropertyRule {

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
     * 在该事件中是否必填
     */
    private Boolean isRequired;

    /**
     * 是否敏感
     */
    private Boolean isSensitive;

    /**
     * 枚举值
     */
    private List<String> enumValues;

    /**
     * 最大长度
     */
    private Integer maxLength;

    /**
     * 校验规则（正则）
     */
    private String validationRule;

    /**
     * 默认值
     */
    private String defaultValue;
}

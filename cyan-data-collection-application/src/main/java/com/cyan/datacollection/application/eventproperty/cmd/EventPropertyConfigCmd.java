package com.cyan.datacollection.application.eventproperty.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 事件属性配置命令
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EventPropertyConfigCmd {

    /**
     * 属性ID
     */
    private String propertyId;

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
     * 在该事件中的说明
     */
    private String description;
}

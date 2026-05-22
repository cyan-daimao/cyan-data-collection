package com.cyan.datacollection.adapter.event.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 事件属性配置请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EventPropertyConfigRequest {

    /**
     * 属性ID
     */
    @NotBlank(message = "属性ID不能为空")
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

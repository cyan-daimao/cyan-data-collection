package com.cyan.datacollection.adapter.plan.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 方案事件属性配置请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingPlanEventConfigRequest {

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
     * 说明
     */
    private String description;
}

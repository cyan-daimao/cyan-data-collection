package com.cyan.datacollection.application.plan.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 方案事件属性配置命令
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingPlanEventConfigCmd {

    private String propertyId;
    private Boolean isRequired;
    private String defaultValue;
    private String sampleValue;
    private String description;
}

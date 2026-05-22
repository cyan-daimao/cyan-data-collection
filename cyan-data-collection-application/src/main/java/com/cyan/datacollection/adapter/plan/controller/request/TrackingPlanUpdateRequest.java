package com.cyan.datacollection.adapter.plan.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 更新方案请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingPlanUpdateRequest {

    /**
     * 方案名称
     */
    private String planName;

    /**
     * 关联需求ID
     */
    private String demandId;

    /**
     * 方案描述
     */
    private String description;
}

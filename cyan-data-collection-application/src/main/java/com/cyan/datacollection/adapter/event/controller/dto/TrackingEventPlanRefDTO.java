package com.cyan.datacollection.adapter.event.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 事件关联方案DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingEventPlanRefDTO {
    /**
     * 方案ID
     */
    private String planId;

    /**
     * 方案编码
     */
    private String planCode;

    /**
     * 方案名称
     */
    private String planName;
}

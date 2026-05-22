package com.cyan.datacollection.adapter.event.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 事件使用情况DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingEventUsageDTO {

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 关联方案数
     */
    private Integer planCount;

    /**
     * 关联方案列表
     */
    private List<TrackingEventPlanRefDTO> plans;

    /**
     * 近7天上报量
     */
    private Long recentSampleCount;

    /**
     * 关联属性数
     */
    private Integer propertyCount;
}

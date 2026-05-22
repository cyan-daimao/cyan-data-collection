package com.cyan.datacollection.adapter.property.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 属性使用情况DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingPropertyUsageDTO {

    /**
     * 属性ID
     */
    private String propertyId;

    /**
     * 关联事件数
     */
    private Integer eventCount;

    /**
     * 关联事件列表
     */
    private List<TrackingPropertyEventRefDTO> events;
}

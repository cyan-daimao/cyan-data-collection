package com.cyan.datacollection.adapter.plan.controller.dto;

import com.cyan.datacollection.enums.PlanStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 埋点方案DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingPlanDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 方案编号
     */
    private String planCode;

    /**
     * 方案名称
     */
    private String planName;

    /**
     * 关联需求ID
     */
    private String demandId;

    /**
     * 关联需求编号
     */
    private String demandCode;

    /**
     * 方案版本
     */
    private Integer version;

    /**
     * 方案描述
     */
    private String description;

    /**
     * 状态
     */
    private PlanStatus status;

    /**
     * 评审人
     */
    private String reviewer;

    /**
     * 已发布版本ID
     */
    private String publishedVersionId;

    /**
     * 方案内事件列表
     */
    private List<TrackingPlanEventDTO> events;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;
}

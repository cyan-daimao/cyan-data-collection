package com.cyan.datacollection.adapter.release.controller.dto;

import com.cyan.datacollection.enums.ReleaseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 埋点发布版本DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingReleaseDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 发布编号
     */
    private String releaseCode;

    /**
     * 方案ID
     */
    private String planId;

    /**
     * 发布版本
     */
    private Integer version;

    /**
     * 状态
     */
    private ReleaseStatus status;

    /**
     * 变更摘要JSON
     */
    private String diffSummary;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishedAt;

    /**
     * 明细列表
     */
    private List<TrackingReleaseItemDTO> items;

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

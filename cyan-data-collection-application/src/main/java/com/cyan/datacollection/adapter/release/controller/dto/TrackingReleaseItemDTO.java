package com.cyan.datacollection.adapter.release.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 埋点发布版本明细DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingReleaseItemDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 发布ID
     */
    private String releaseId;

    /**
     * 对象类型
     */
    private String itemType;

    /**
     * 对象ID
     */
    private String itemId;

    /**
     * 对象编码
     */
    private String itemCode;

    /**
     * 变更类型
     */
    private String changeType;

    /**
     * 发布快照JSON
     */
    private String snapshot;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
}

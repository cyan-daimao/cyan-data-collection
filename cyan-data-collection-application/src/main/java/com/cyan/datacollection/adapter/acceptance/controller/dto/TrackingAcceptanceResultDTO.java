package com.cyan.datacollection.adapter.acceptance.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 验收结果DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingAcceptanceResultDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 验收任务ID
     */
    private String taskId;

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 状态: PASS, FAIL
     */
    private String status;

    /**
     * 错误项列表
     */
    private List<String> errorItems;

    /**
     * 命中的样本ID列表
     */
    private List<String> sampleIds;

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

package com.cyan.datacollection.application.acceptance.bo;

import com.cyan.datacollection.enums.AcceptanceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 验收任务业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingAcceptanceTaskBO {

    /**
     * 主键
     */
    private String id;

    /**
     * 验收任务编号
     */
    private String taskCode;

    /**
     * 方案ID
     */
    private String planId;

    /**
     * Debug Token
     */
    private String debugToken;

    /**
     * 环境
     */
    private String environment;

    /**
     * 状态
     */
    private AcceptanceStatus status;

    /**
     * 事件覆盖率
     */
    private BigDecimal eventCoverageRate;

    /**
     * 必填属性完整率
     */
    private BigDecimal requiredPropertyCompleteRate;

    /**
     * 类型正确率
     */
    private BigDecimal typeValidRate;

    /**
     * 结果摘要JSON
     */
    private String resultSummary;

    /**
     * 验收结果列表
     */
    private List<TrackingAcceptanceResultBO> results;

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

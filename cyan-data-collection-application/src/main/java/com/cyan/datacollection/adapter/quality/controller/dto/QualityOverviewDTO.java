package com.cyan.datacollection.adapter.quality.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 事件质量总览DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QualityOverviewDTO {

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 总上报量
     */
    private Long totalCount;

    /**
     * 通过量
     */
    private Long passCount;

    /**
     * 警告量
     */
    private Long warnCount;

    /**
     * 失败量
     */
    private Long failCount;

    /**
     * 通过率
     */
    private BigDecimal passRate;
}

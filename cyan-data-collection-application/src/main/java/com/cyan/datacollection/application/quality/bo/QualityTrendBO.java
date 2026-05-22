package com.cyan.datacollection.application.quality.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 事件质量趋势业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QualityTrendBO {

    /**
     * 时间维度（小时）
     */
    private String timeHour;

    /**
     * 通过数量
     */
    private Long passCount;

    /**
     * 警告数量
     */
    private Long warnCount;

    /**
     * 失败数量
     */
    private Long failCount;
}

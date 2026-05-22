package com.cyan.datacollection.application.quality.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 质量总览查询条件
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QualityOverviewQuery {

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 事件编码
     */
    private String eventCode;
}

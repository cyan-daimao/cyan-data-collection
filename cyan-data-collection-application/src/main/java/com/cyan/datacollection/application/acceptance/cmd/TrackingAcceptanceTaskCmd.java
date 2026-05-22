package com.cyan.datacollection.application.acceptance.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 验收任务命令
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingAcceptanceTaskCmd {

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
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;
}

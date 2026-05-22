package com.cyan.datacollection.application.app.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 接入应用命令
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingAppCmd {

    private String appCode;
    private String appName;
    private String appType;
    private String description;
    private String createBy;
    private String updateBy;
}

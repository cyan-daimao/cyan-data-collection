package com.cyan.datacollection.application.debug.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 创建 Debug 会话命令
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DebugSessionCmd {

    private String appCode;
    private String userId;
    private String anonymousId;
    private String deviceId;
    private String environment;
    private LocalDateTime expiredAt;
    private String createBy;
}

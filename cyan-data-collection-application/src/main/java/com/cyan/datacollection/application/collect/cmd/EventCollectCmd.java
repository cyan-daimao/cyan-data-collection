package com.cyan.datacollection.application.collect.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 事件上报命令
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EventCollectCmd {

    private String appCode;
    private String debugToken;
    private String eventCode;
    private LocalDateTime eventTime;
    private String terminalType;
    private String environment;
    private String userId;
    private String anonymousId;
    private String sessionId;
    private String deviceId;
    private String sdkVersion;
    private String appVersion;
    private String pageCode;
    private String requestId;
    private Map<String, Object> properties;
}

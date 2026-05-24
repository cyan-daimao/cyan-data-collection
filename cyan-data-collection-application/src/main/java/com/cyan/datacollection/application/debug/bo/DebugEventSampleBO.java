package com.cyan.datacollection.application.debug.bo;

import com.cyan.datacollection.enums.Environment;
import com.cyan.datacollection.enums.TerminalType;
import com.cyan.datacollection.enums.ValidateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Debug 事件样本业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DebugEventSampleBO {

    private String id;
    private String appCode;
    private String debugToken;
    private String eventCode;
    private LocalDateTime eventTime;
    private LocalDateTime ingestionTime;
    private TerminalType terminalType;
    private Environment environment;
    private String userId;
    private String anonymousId;
    private String sessionId;
    private String deviceId;
    private String sdkVersion;
    private String appVersion;
    private String pageCode;
    private String requestId;
    private String common;
    private String action;
    private String business;
    private String extra;
    private String payload;
    private ValidateStatus validateStatus;
    private List<String> validateErrors;
    private LocalDateTime createdAt;
}

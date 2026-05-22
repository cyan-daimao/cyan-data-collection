package com.cyan.datacollection.application.debug.bo;

import com.cyan.datacollection.enums.DebugSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Debug 会话业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DebugSessionBO {

    private String id;
    private String debugToken;
    private String appCode;
    private String userId;
    private String anonymousId;
    private String deviceId;
    private String environment;
    private LocalDateTime expiredAt;
    private DebugSessionStatus status;
    private String createBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

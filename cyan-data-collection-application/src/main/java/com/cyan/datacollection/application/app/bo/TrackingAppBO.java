package com.cyan.datacollection.application.app.bo;

import com.cyan.datacollection.enums.AppStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 接入应用业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingAppBO {

    private String id;
    private String appCode;
    private String appName;
    private String appType;
    private String description;
    private String reportUrl;
    private AppStatus status;
    private String createBy;
    private String updateBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class IntegrationBO {
        private String appCode;
        private String appName;
        private String reportUrl;
        private String jsSdkCode;
        private String javaSdkCode;
    }
}

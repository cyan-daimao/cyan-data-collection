package com.cyan.datacollection.application.event.bo;

import com.cyan.datacollection.enums.EventStatus;
import com.cyan.datacollection.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 事件定义业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingEventBO {

    private String id;

    private String eventCode;

    private String eventName;

    private EventType eventType;

    private String businessDomain;

    private String description;

    private String triggerTiming;

    private List<String> terminalTypes;

    private String owner;

    private Boolean isCore;

    private EventStatus status;

    private Integer version;

    private String createBy;

    private String updateBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class UsageBO {
        private String eventId;
        private Integer planCount;
        private List<PlanRefBO> plans;
        private Long recentSampleCount;
        private Integer propertyCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class PlanRefBO {
        private String planId;
        private String planCode;
        private String planName;
    }
}

package com.cyan.datacollection.application.property.bo;

import com.cyan.datacollection.enums.DataType;
import com.cyan.datacollection.enums.PropertyStatus;
import com.cyan.datacollection.enums.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 属性定义业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingPropertyBO {

    private String id;
    private String propertyCode;
    private String propertyName;
    private PropertyType propertyType;
    private DataType dataType;
    private String description;
    private Boolean isRequired;
    private Boolean isSensitive;
    private String securityLevel;
    private List<String> enumValues;
    private Integer maxLength;
    private String validationRule;
    private String standardCode;
    private PropertyStatus status;
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
        private String propertyId;
        private Integer eventCount;
        private List<EventRefBO> events;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class EventRefBO {
        private String eventId;
        private String eventCode;
        private String eventName;
    }
}

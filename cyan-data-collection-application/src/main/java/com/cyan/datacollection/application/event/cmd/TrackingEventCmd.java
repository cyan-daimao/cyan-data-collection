package com.cyan.datacollection.application.event.cmd;

import com.cyan.datacollection.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 事件定义命令
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingEventCmd {

    private String eventCode;

    private String eventName;

    private EventType eventType;

    private String businessDomain;

    private String description;

    private String triggerTiming;

    private List<String> terminalTypes;

    private String owner;

    private Boolean isCore;

    private String createBy;

    private String updateBy;
}

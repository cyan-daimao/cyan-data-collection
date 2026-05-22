package com.cyan.datacollection.domain.event.query;

import com.cyan.arch.common.api.Pageable;
import lombok.Data;

/**
 * 事件分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingEventDomainQuery implements Pageable {

    private long pageNum = 1;
    private long pageSize = 20;
    private String eventCode;
    private String eventName;
    private String eventType;
    private String businessDomain;
    private String status;
    private Boolean isCore;

    @Override
    public long current() {
        return pageNum;
    }

    @Override
    public long size() {
        return pageSize;
    }
}

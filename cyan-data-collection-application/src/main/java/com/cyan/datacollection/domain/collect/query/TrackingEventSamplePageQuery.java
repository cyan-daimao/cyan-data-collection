package com.cyan.datacollection.domain.collect.query;

import com.cyan.arch.common.api.Pageable;
import lombok.Data;

/**
 * 事件样本分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingEventSamplePageQuery implements Pageable {

    private long pageNum = 1;
    private long pageSize = 20;
    private String debugToken;
    private String appCode;
    private String eventCode;
    private String environment;

    @Override
    public long current() {
        return pageNum;
    }

    @Override
    public long size() {
        return pageSize;
    }
}

package com.cyan.datacollection.domain.demand.query;

import com.cyan.arch.common.api.Pageable;
import lombok.Data;

/**
 * 需求分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingDemandPageQuery implements Pageable {

    private long pageNum = 1;
    private long pageSize = 20;
    private String demandCode;
    private String demandName;
    private String businessDomain;
    private String priority;
    private String status;

    @Override
    public long current() {
        return pageNum;
    }

    @Override
    public long size() {
        return pageSize;
    }
}

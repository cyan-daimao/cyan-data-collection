package com.cyan.datacollection.domain.property.query;

import com.cyan.arch.common.api.Pageable;
import lombok.Data;

/**
 * 属性分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingPropertyPageQuery implements Pageable {

    private long pageNum = 1;
    private long pageSize = 20;
    private String propertyCode;
    private String propertyName;
    private String propertyType;
    private String dataType;
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

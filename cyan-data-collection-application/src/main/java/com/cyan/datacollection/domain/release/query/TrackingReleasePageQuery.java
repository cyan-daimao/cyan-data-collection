package com.cyan.datacollection.domain.release.query;

import com.cyan.arch.common.api.Pageable;
import lombok.Data;

/**
 * 埋点发布版本分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingReleasePageQuery implements Pageable {

    /**
     * 页码
     */
    private long pageNum = 1;

    /**
     * 页大小
     */
    private long pageSize = 20;

    /**
     * 方案ID
     */
    private String planId;

    /**
     * 发布编号（模糊）
     */
    private String releaseCode;

    /**
     * 状态
     */
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

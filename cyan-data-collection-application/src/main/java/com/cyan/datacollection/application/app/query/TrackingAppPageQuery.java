package com.cyan.datacollection.application.app.query;

import com.cyan.arch.common.api.Pageable;
import lombok.Data;

/**
 * 接入应用分页查询（Application层）
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingAppPageQuery implements Pageable {

    /**
     * 页码
     */
    private long pageNum = 1;

    /**
     * 页大小
     */
    private long pageSize = 20;

    /**
     * 应用编码（模糊）
     */
    private String appCode;

    /**
     * 应用名称（模糊）
     */
    private String appName;

    /**
     * 应用类型
     */
    private String appType;

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

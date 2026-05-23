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
public class TrackingEventPageQuery implements Pageable {

    /**
     * 页码
     */
    private long pageNum = 1;

    /**
     * 页大小
     */
    private long pageSize = 20;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 业务域
     */
    private String businessDomain;

    /**
     * 状态
     */
    private String status;

    /**
     * 是否核心事件
     */
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

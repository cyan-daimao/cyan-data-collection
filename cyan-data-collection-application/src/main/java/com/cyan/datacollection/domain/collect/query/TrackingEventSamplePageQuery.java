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

    /**
     * 页码
     */
    private long pageNum = 1;

    /**
     * 页大小
     */
    private long pageSize = 20;

    /**
     * Debug Token
     */
    private String debugToken;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 环境
     */
    private String environment;

    /**
     * 员工ID
     */
    private String employeeId;

    /**
     * 匿名ID
     */
    private String anonymousId;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    @Override
    public long current() {
        return pageNum;
    }

    @Override
    public long size() {
        return pageSize;
    }
}

package com.cyan.datacollection.application.debug.query;

import com.cyan.arch.common.api.Pageable;
import lombok.Data;

/**
 * Debug 事件样本分页查询（Application层）
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class DebugEventSamplePageQuery implements Pageable {

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

    @Override
    public long current() {
        return pageNum;
    }

    @Override
    public long size() {
        return pageSize;
    }
}

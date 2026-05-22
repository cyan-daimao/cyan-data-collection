package com.cyan.datacollection.domain.quality.query;

import com.cyan.arch.common.api.Pageable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 告警分页查询
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingAlertPageQuery implements Pageable {

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 告警类型
     */
    private String alertType;

    /**
     * 告警级别
     */
    private String alertLevel;

    /**
     * 状态
     */
    private String status;

    /**
     * 页码
     */
    private long current = 1;

    /**
     * 每页大小
     */
    private long size = 10;

    @Override
    public long current() {
        return current;
    }

    @Override
    public long size() {
        return size;
    }
}

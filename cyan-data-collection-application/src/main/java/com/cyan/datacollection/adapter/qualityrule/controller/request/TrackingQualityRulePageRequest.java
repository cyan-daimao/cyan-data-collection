package com.cyan.datacollection.adapter.qualityrule.controller.request;

import com.cyan.arch.common.api.Pageable;
import lombok.Data;

/**
 * 质量规则配置分页请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
public class TrackingQualityRulePageRequest implements Pageable {

    private long pageNum = 1;
    private long pageSize = 20;
    private String ruleCode;
    private String ruleName;
    private String eventCode;
    private String appCode;
    private String alertType;
    private String alertLevel;
    private Boolean isEnabled;

    @Override
    public long current() {
        return pageNum;
    }

    @Override
    public long size() {
        return pageSize;
    }
}

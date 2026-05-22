package com.cyan.datacollection.adapter.demand.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 更新需求请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingDemandUpdateRequest {

    /**
     * 需求名称
     */
    private String demandName;

    /**
     * 业务域
     */
    private String businessDomain;

    /**
     * 产品线
     */
    private String productLine;

    /**
     * 涉及端类型
     */
    private List<String> terminalTypes;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 业务目标
     */
    private String businessGoal;

    /**
     * 分析目标
     */
    private String analysisGoal;

    /**
     * 产品负责人
     */
    private String productOwner;

    /**
     * 技术负责人
     */
    private String techOwner;

    /**
     * 测试负责人
     */
    private String testOwner;

    /**
     * 数据负责人
     */
    private String dataOwner;

    /**
     * 期望上线日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate expectedReleaseDate;
}

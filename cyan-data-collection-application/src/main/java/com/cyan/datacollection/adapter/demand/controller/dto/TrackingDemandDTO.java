package com.cyan.datacollection.adapter.demand.controller.dto;

import com.cyan.datacollection.enums.DemandStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 埋点需求DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingDemandDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 需求编号
     */
    private String demandCode;

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

    /**
     * 状态
     */
    private DemandStatus status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;
}

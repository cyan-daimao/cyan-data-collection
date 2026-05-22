package com.cyan.datacollection.infra.persistence.demand.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyan.datacollection.enums.DemandStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 埋点需求表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("tracking_demand")
public class TrackingDemandDO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 需求编码
     */
    @TableField("demand_code")
    private String demandCode;

    /**
     * 需求名称
     */
    @TableField("demand_name")
    private String demandName;

    /**
     * 业务域
     */
    @TableField("business_domain")
    private String businessDomain;

    /**
     * 产品线
     */
    @TableField("product_line")
    private String productLine;

    /**
     * 涉及端类型
     */
    @TableField("terminal_types")
    private String terminalTypes;

    /**
     * 优先级
     */
    @TableField("priority")
    private String priority;

    /**
     * 业务目标
     */
    @TableField("business_goal")
    private String businessGoal;

    /**
     * 分析目标
     */
    @TableField("analysis_goal")
    private String analysisGoal;

    /**
     * 产品负责人
     */
    @TableField("product_owner")
    private String productOwner;

    /**
     * 技术负责人
     */
    @TableField("tech_owner")
    private String techOwner;

    /**
     * 测试负责人
     */
    @TableField("test_owner")
    private String testOwner;

    /**
     * 数据负责人
     */
    @TableField("data_owner")
    private String dataOwner;

    /**
     * 期望上线日期
     */
    @TableField("expected_release_date")
    private LocalDate expectedReleaseDate;

    /**
     * 状态
     */
    @TableField("status")
    private DemandStatus status;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private String updateBy;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 删除时间
     */
    @TableField("deleted_at")
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;
}

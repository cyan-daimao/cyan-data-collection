package com.cyan.datacollection.domain.demand;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.demand.repository.TrackingDemandRepository;
import com.cyan.datacollection.enums.DemandStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 埋点需求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingDemand {

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
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除时间
     */
    private LocalDateTime deletedAt;

    private void validate() {
        Assert.notBlank(this.demandName, new SilentException("需求名称不能为空"));
    }

    public TrackingDemand save(TrackingDemandRepository repository) {
        validate();
        this.status = DemandStatus.DRAFT;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    public TrackingDemand update(TrackingDemandRepository repository) {
        validate();
        Assert.notBlank(this.id, new SilentException("需求ID不能为空"));
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    public TrackingDemand submitDesign(TrackingDemandRepository repository) {
        Assert.notNull(this.status, new SilentException("需求状态异常"));
        if (this.status != DemandStatus.DRAFT && this.status != DemandStatus.DESIGNING) {
            throw new SilentException("当前状态不可提交设计");
        }
        this.status = DemandStatus.REVIEWING;
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    public TrackingDemand close(TrackingDemandRepository repository) {
        Assert.notNull(this.status, new SilentException("需求状态异常"));
        if (this.status == DemandStatus.CLOSED) {
            throw new SilentException("需求已关闭");
        }
        this.status = DemandStatus.CLOSED;
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    public void delete(TrackingDemandRepository repository) {
        Assert.notBlank(this.id, new SilentException("需求ID不能为空"));
        repository.deleteById(this.id);
    }
}

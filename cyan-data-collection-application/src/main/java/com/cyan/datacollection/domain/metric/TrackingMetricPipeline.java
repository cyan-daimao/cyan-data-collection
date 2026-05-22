package com.cyan.datacollection.domain.metric;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.metric.repository.TrackingMetricPipelineRepository;
import com.cyan.datacollection.enums.MetricPipelineStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 采集指标链路
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingMetricPipeline {

    private String id;
    private String metricCode;
    private String metricName;
    private String eventCode;
    private String appCode;
    private String dimensionsJson;
    private String measuresJson;
    private String topicName;
    private String odsTableName;
    private String dwdTableName;
    private String dwsTableName;
    private String adsTableName;
    private String dataworksJobId;
    private String dataworksInstanceId;
    private String flinkDeploymentName;
    private MetricPipelineStatus status;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String createBy;
    private String updateBy;

    private void validate() {
        Assert.notBlank(this.metricCode, new SilentException("指标编码不能为空"));
        Assert.notBlank(this.metricName, new SilentException("指标名称不能为空"));
        Assert.notBlank(this.eventCode, new SilentException("事件编码不能为空"));
        Assert.notBlank(this.topicName, new SilentException("Topic名称不能为空"));
        Assert.notBlank(this.odsTableName, new SilentException("ODS表名不能为空"));
        Assert.notBlank(this.dwdTableName, new SilentException("DWD表名不能为空"));
        Assert.notBlank(this.dwsTableName, new SilentException("DWS表名不能为空"));
        Assert.notBlank(this.adsTableName, new SilentException("ADS表名不能为空"));
    }

    public TrackingMetricPipeline save(TrackingMetricPipelineRepository repository) {
        validate();
        this.status = MetricPipelineStatus.DRAFT;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    public TrackingMetricPipeline update(TrackingMetricPipelineRepository repository) {
        validate();
        Assert.notBlank(this.id, new SilentException("ID不能为空"));
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    public void delete(TrackingMetricPipelineRepository repository) {
        Assert.notBlank(this.id, new SilentException("ID不能为空"));
        repository.deleteById(this.id);
    }
}

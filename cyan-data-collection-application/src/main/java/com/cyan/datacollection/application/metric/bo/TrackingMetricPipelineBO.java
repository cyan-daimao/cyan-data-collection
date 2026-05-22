package com.cyan.datacollection.application.metric.bo;

import com.cyan.datacollection.enums.MetricPipelineStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 采集指标链路业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingMetricPipelineBO {

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
    private String createBy;
    private String updateBy;
    private List<String> dimensions;
    private List<MeasureBO> measures;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class MeasureBO {
        private String name;
        private String expr;
    }
}

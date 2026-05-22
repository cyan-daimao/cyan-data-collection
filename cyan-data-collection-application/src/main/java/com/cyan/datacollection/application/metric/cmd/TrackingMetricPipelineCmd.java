package com.cyan.datacollection.application.metric.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * 采集指标链路命令对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingMetricPipelineCmd {

    private String metricCode;
    private String metricName;
    private String eventCode;
    private String appCode;
    private List<String> dimensions;
    private List<MeasureCmd> measures;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class MeasureCmd {
        private String name;
        private String expr;
    }
}

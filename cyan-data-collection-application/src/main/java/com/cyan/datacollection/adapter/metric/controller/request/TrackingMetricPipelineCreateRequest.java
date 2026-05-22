package com.cyan.datacollection.adapter.metric.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * 采集指标链路创建请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingMetricPipelineCreateRequest {

    @NotBlank(message = "指标编码不能为空")
    private String metricCode;

    @NotBlank(message = "指标名称不能为空")
    private String metricName;

    @NotBlank(message = "事件编码不能为空")
    private String eventCode;

    private String appCode;
    private List<String> dimensions;
    private List<MeasureRequest> measures;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class MeasureRequest {
        private String name;
        private String expr;
    }
}

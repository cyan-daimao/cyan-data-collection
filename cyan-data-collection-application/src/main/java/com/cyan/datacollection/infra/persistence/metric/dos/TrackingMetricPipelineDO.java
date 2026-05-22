package com.cyan.datacollection.infra.persistence.metric.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyan.datacollection.enums.MetricPipelineStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 采集指标链路表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("tracking_metric_pipeline")
public class TrackingMetricPipelineDO {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("metric_code")
    private String metricCode;

    @TableField("metric_name")
    private String metricName;

    @TableField("event_code")
    private String eventCode;

    @TableField("app_code")
    private String appCode;

    @TableField("dimensions_json")
    private String dimensionsJson;

    @TableField("measures_json")
    private String measuresJson;

    @TableField("topic_name")
    private String topicName;

    @TableField("ods_table_name")
    private String odsTableName;

    @TableField("dwd_table_name")
    private String dwdTableName;

    @TableField("dws_table_name")
    private String dwsTableName;

    @TableField("ads_table_name")
    private String adsTableName;

    @TableField("dataworks_job_id")
    private String dataworksJobId;

    @TableField("dataworks_instance_id")
    private String dataworksInstanceId;

    @TableField("flink_deployment_name")
    private String flinkDeploymentName;

    @TableField("status")
    private MetricPipelineStatus status;

    @TableField("error_message")
    private String errorMessage;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField("deleted_at")
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;

    @TableField("created_by")
    private String createBy;

    @TableField("updated_by")
    private String updateBy;
}

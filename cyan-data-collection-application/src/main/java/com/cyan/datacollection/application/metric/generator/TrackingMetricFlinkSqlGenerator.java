package com.cyan.datacollection.application.metric.generator;

import com.cyan.datacollection.application.metric.cmd.TrackingMetricPipelineCmd;
import com.cyan.datacollection.domain.metric.TrackingMetricPipeline;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采集指标 FlinkSQL 生成器
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Component
@Deprecated(since = "1.0.0", forRemoval = false)
public class TrackingMetricFlinkSqlGenerator {

    @Value("${tracking.kafka.bootstrap-servers:kafka:9092}")
    private String kafkaBootstrapServers;

    @Value("${tracking.iceberg.rest-uri:http://iceberg-rest.cyan.com/iceberg}")
    private String icebergRestUri;

    @Value("${rustfs.endpoint:http://10.0.0.2:9000}")
    private String rustfsEndpoint;

    @Value("${rustfs.accessKey:rustfsadmin}")
    private String rustfsAccessKey;

    @Value("${rustfs.secretKey:rustfsadmin}")
    private String rustfsSecretKey;

    /**
     * 生成完整的 FlinkSQL 脚本
     */
    public String generate(TrackingMetricPipeline pipeline, List<String> dimensions,
                           List<TrackingMetricPipelineCmd.MeasureCmd> measures) {
        StringBuilder sql = new StringBuilder();

        // 1. Catalog
        sql.append("CREATE CATALOG IF NOT EXISTS rest WITH (\n")
           .append("  'type' = 'iceberg',\n")
           .append("  'catalog-type' = 'rest',\n")
           .append("  'uri' = '").append(icebergRestUri).append("',\n")
           .append("  's3.endpoint' = '").append(rustfsEndpoint).append("',\n")
           .append("  's3.access-key-id' = '").append(rustfsAccessKey).append("',\n")
           .append("  's3.secret-access-key' = '").append(rustfsSecretKey).append("',\n")
           .append("  's3.path-style-access' = 'true'\n")
           .append(");\n\n");

        // 2. Kafka Source
        sql.append(buildKafkaSource(pipeline));

        // 3. Iceberg Sink DDL
        sql.append(buildSinkTables(pipeline, dimensions, measures));

        // 4. ODS INSERT
        sql.append(buildOdsInsert(pipeline));

        // 5. DWD INSERT
        sql.append(buildDwdInsert(pipeline, dimensions));

        // 6. DWS INSERT
        sql.append(buildDwsInsert(pipeline, dimensions, measures));

        // 7. ADS INSERT
        sql.append(buildAdsInsert(pipeline, dimensions, measures));

        return sql.toString();
    }

    private String buildKafkaSource(TrackingMetricPipeline pipeline) {
        return String.format("""
                CREATE TABLE IF NOT EXISTS kafka_source (
                  raw STRING
                ) WITH (
                  'connector' = 'kafka',
                  'topic' = 'cyan_data_collection_event_raw',
                  'properties.bootstrap.servers' = '%s',
                  'properties.group.id' = 'flink-data-collection-%s',
                  'scan.startup.mode' = 'earliest-offset',
                  'format' = 'raw'
                );
                
                """, kafkaBootstrapServers, pipeline.getMetricCode());
    }

    private String buildSinkTables(TrackingMetricPipeline pipeline, List<String> dimensions,
                                   List<TrackingMetricPipelineCmd.MeasureCmd> measures) {
        return buildOdsTable(pipeline)
                + buildDwdTable(pipeline, dimensions)
                + buildMetricTable("dws", pipeline.getDwsTableName(), dimensions, measures)
                + buildMetricTable("ads", pipeline.getAdsTableName(), dimensions, measures);
    }

    private String buildOdsTable(TrackingMetricPipeline pipeline) {
        return String.format("""
                CREATE TABLE IF NOT EXISTS rest.ods.%s (
                  request_id STRING,
                  app_code STRING,
                  event_code STRING,
                  event_time STRING,
                  ingestion_time STRING,
                  debug_token STRING,
                  common STRING,
                  action STRING,
                  business STRING,
                  extra STRING,
                  payload STRING,
                  validate_status STRING,
                  validate_errors STRING,
                  dt STRING
                );
                
                """, pipeline.getOdsTableName());
    }

    private String buildDwdTable(TrackingMetricPipeline pipeline, List<String> dimensions) {
        String dimColumns = dimensions.stream()
                .map(d -> "  " + d + " STRING,")
                .collect(Collectors.joining("\n"));
        if (!dimColumns.isEmpty()) {
            dimColumns = ",\n" + dimColumns;
        }
        return String.format("""
                CREATE TABLE IF NOT EXISTS rest.dwd.%s (
                  request_id STRING,
                  app_code STRING,
                  event_code STRING,
                  event_time TIMESTAMP(3),
                  employee_id STRING,
                  anonymous_id STRING,
                  session_id STRING%s,
                  dt STRING
                );
                
                """, pipeline.getDwdTableName(), dimColumns);
    }

    private String buildMetricTable(String schema, String tableName, List<String> dimensions,
                                    List<TrackingMetricPipelineCmd.MeasureCmd> measures) {
        List<String> columns = new ArrayList<>();
        dimensions.forEach(d -> columns.add("  " + d + " STRING"));
        measures.forEach(m -> columns.add("  " + m.getName() + " BIGINT"));
        String dynamicColumns = columns.isEmpty() ? "" : ",\n" + String.join(",\n", columns);
        return String.format("""
                CREATE TABLE IF NOT EXISTS rest.%s.%s (
                  dt STRING,
                  app_code STRING%s
                );
                
                """, schema, tableName, dynamicColumns);
    }

    private String buildOdsInsert(TrackingMetricPipeline pipeline) {
        return String.format("""
                INSERT INTO rest.ods.%s
                SELECT
                  JSON_VALUE(raw, '$.request_id') AS request_id,
                  JSON_VALUE(raw, '$.app_code') AS app_code,
                  JSON_VALUE(raw, '$.event_code') AS event_code,
                  JSON_VALUE(raw, '$.event_time') AS event_time,
                  JSON_VALUE(raw, '$.ingestion_time') AS ingestion_time,
                  JSON_VALUE(raw, '$.debug_token') AS debug_token,
                  JSON_VALUE(raw, '$.common') AS common,
                  JSON_VALUE(raw, '$.action') AS action,
                  JSON_VALUE(raw, '$.business') AS business,
                  JSON_VALUE(raw, '$.extra') AS extra,
                  JSON_VALUE(raw, '$.payload') AS payload,
                  JSON_VALUE(raw, '$.validate_status') AS validate_status,
                  JSON_VALUE(raw, '$.validate_errors') AS validate_errors,
                  SUBSTRING(JSON_VALUE(raw, '$.event_time'), 1, 10) AS dt
                FROM kafka_source
                WHERE JSON_VALUE(raw, '$.event_code') = '%s';
                
                """, pipeline.getOdsTableName(), pipeline.getEventCode());
    }

    private String buildDwdInsert(TrackingMetricPipeline pipeline, List<String> dimensions) {
        String dimSelect = dimensions.stream()
                .map(d -> String.format("  %s AS %s,", jsonSectionValue(d), d))
                .collect(Collectors.joining("\n"));
        if (!dimSelect.isEmpty()) {
            dimSelect = ",\n" + dimSelect;
        }

        return String.format("""
                INSERT INTO rest.dwd.%s
                SELECT
                  JSON_VALUE(raw, '$.request_id') AS request_id,
                  JSON_VALUE(raw, '$.app_code') AS app_code,
                  JSON_VALUE(raw, '$.event_code') AS event_code,
                  CAST(JSON_VALUE(raw, '$.event_time') AS TIMESTAMP) AS event_time,
                  JSON_VALUE(JSON_VALUE(raw, '$.business'), '$.employee_id') AS employee_id,
                  JSON_VALUE(JSON_VALUE(raw, '$.common'), '$.anonymous_id') AS anonymous_id,
                  JSON_VALUE(JSON_VALUE(raw, '$.common'), '$.session_id') AS session_id%s,
                  SUBSTRING(JSON_VALUE(raw, '$.event_time'), 1, 10) AS dt
                FROM kafka_source
                WHERE JSON_VALUE(raw, '$.event_code') = '%s';
                
                """, pipeline.getDwdTableName(), dimSelect, pipeline.getEventCode());
    }

    private String buildDwsInsert(TrackingMetricPipeline pipeline, List<String> dimensions,
                                   List<TrackingMetricPipelineCmd.MeasureCmd> measures) {
        String dimSelect = dimensions.stream()
                .map(d -> String.format("  %s AS %s", jsonSectionValue(d), d))
                .collect(Collectors.joining(",\n"));
        if (!dimSelect.isEmpty()) {
            dimSelect = dimSelect + ",\n";
        }

        String dimGroupBy = dimensions.stream()
                .map(this::jsonSectionValue)
                .collect(Collectors.joining(", "));
        if (!dimGroupBy.isEmpty()) {
            dimGroupBy = ", " + dimGroupBy;
        }

        String measureSelect = measures.stream()
                .map(m -> String.format("  %s AS %s", normalizeMeasureExpr(m.getExpr()), m.getName()))
                .collect(Collectors.joining(",\n"));

        return String.format("""
                INSERT INTO rest.dws.%s
                SELECT
                  SUBSTRING(JSON_VALUE(raw, '$.event_time'), 1, 10) AS dt,
                  JSON_VALUE(raw, '$.app_code') AS app_code,
                  %s
                  %s
                FROM kafka_source
                WHERE JSON_VALUE(raw, '$.event_code') = '%s'
                GROUP BY SUBSTRING(JSON_VALUE(raw, '$.event_time'), 1, 10), JSON_VALUE(raw, '$.app_code')%s;
                
                """, pipeline.getDwsTableName(), dimSelect, measureSelect,
                pipeline.getEventCode(), dimGroupBy);
    }

    private String buildAdsInsert(TrackingMetricPipeline pipeline, List<String> dimensions,
                                   List<TrackingMetricPipelineCmd.MeasureCmd> measures) {
        // 第一阶段 ADS 和 DWS 逻辑一致
        return buildDwsInsert(pipeline, dimensions, measures)
                .replace("rest.dws." + pipeline.getDwsTableName(), "rest.ads." + pipeline.getAdsTableName());
    }

    private String normalizeMeasureExpr(String expr) {
        if (expr == null || expr.isBlank()) {
            return "COUNT(*)";
        }
        return expr.replace("COUNT(DISTINCT user_id)", "COUNT(DISTINCT JSON_VALUE(JSON_VALUE(raw, '$.business'), '$.employee_id'))")
                .replace("count(distinct user_id)", "COUNT(DISTINCT JSON_VALUE(JSON_VALUE(raw, '$.business'), '$.employee_id'))")
                .replace("COUNT(DISTINCT employee_id)", "COUNT(DISTINCT JSON_VALUE(JSON_VALUE(raw, '$.business'), '$.employee_id'))")
                .replace("count(distinct employee_id)", "COUNT(DISTINCT JSON_VALUE(JSON_VALUE(raw, '$.business'), '$.employee_id'))");
    }

    private String jsonSectionValue(String propertyCode) {
        return String.format(
                "COALESCE(JSON_VALUE(JSON_VALUE(raw, '$.business'), '$.%s'), JSON_VALUE(JSON_VALUE(raw, '$.action'), '$.%s'), JSON_VALUE(JSON_VALUE(raw, '$.common'), '$.%s'), JSON_VALUE(JSON_VALUE(raw, '$.extra'), '$.%s'))",
                propertyCode, propertyCode, propertyCode, propertyCode
        );
    }
}

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
                  event_time TIMESTAMP(3),
                  ingestion_time TIMESTAMP(3),
                  terminal_type STRING,
                  environment STRING,
                  user_id STRING,
                  anonymous_id STRING,
                  session_id STRING,
                  device_id STRING,
                  page_code STRING,
                  debug_token STRING,
                  validate_status STRING,
                  validate_errors STRING,
                  properties STRING,
                  payload STRING,
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
                  user_id STRING,
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
                  JSON_VALUE(raw, '$.requestId') AS request_id,
                  JSON_VALUE(raw, '$.appCode') AS app_code,
                  JSON_VALUE(raw, '$.eventCode') AS event_code,
                  CAST(JSON_VALUE(raw, '$.eventTime') AS TIMESTAMP) AS event_time,
                  CAST(JSON_VALUE(raw, '$.ingestionTime') AS TIMESTAMP) AS ingestion_time,
                  JSON_VALUE(raw, '$.terminalType') AS terminal_type,
                  JSON_VALUE(raw, '$.environment') AS environment,
                  JSON_VALUE(raw, '$.userId') AS user_id,
                  JSON_VALUE(raw, '$.anonymousId') AS anonymous_id,
                  JSON_VALUE(raw, '$.sessionId') AS session_id,
                  JSON_VALUE(raw, '$.deviceId') AS device_id,
                  JSON_VALUE(raw, '$.pageCode') AS page_code,
                  JSON_VALUE(raw, '$.debugToken') AS debug_token,
                  JSON_VALUE(raw, '$.validateStatus') AS validate_status,
                  JSON_QUERY(raw, '$.validateErrors') AS validate_errors,
                  JSON_QUERY(raw, '$.properties') AS properties,
                  raw AS payload,
                  DATE_FORMAT(CURRENT_TIMESTAMP, 'yyyy-MM-dd') AS dt
                FROM kafka_source
                WHERE JSON_VALUE(raw, '$.eventCode') = '%s';
                
                """, pipeline.getOdsTableName(), pipeline.getEventCode());
    }

    private String buildDwdInsert(TrackingMetricPipeline pipeline, List<String> dimensions) {
        String dimSelect = dimensions.stream()
                .map(d -> String.format("  JSON_VALUE(raw, '$.properties.%s') AS %s,", d, d))
                .collect(Collectors.joining("\n"));
        if (!dimSelect.isEmpty()) {
            dimSelect = ",\n" + dimSelect;
        }

        return String.format("""
                INSERT INTO rest.dwd.%s
                SELECT
                  JSON_VALUE(raw, '$.requestId') AS request_id,
                  JSON_VALUE(raw, '$.appCode') AS app_code,
                  JSON_VALUE(raw, '$.eventCode') AS event_code,
                  CAST(JSON_VALUE(raw, '$.eventTime') AS TIMESTAMP) AS event_time,
                  JSON_VALUE(raw, '$.userId') AS user_id,
                  JSON_VALUE(raw, '$.anonymousId') AS anonymous_id,
                  JSON_VALUE(raw, '$.sessionId') AS session_id%s,
                  DATE_FORMAT(CURRENT_TIMESTAMP, 'yyyy-MM-dd') AS dt
                FROM kafka_source
                WHERE JSON_VALUE(raw, '$.eventCode') = '%s';
                
                """, pipeline.getDwdTableName(), dimSelect, pipeline.getEventCode());
    }

    private String buildDwsInsert(TrackingMetricPipeline pipeline, List<String> dimensions,
                                   List<TrackingMetricPipelineCmd.MeasureCmd> measures) {
        String dimSelect = dimensions.stream()
                .map(d -> String.format("  JSON_VALUE(raw, '$.properties.%s') AS %s", d, d))
                .collect(Collectors.joining(",\n"));
        if (!dimSelect.isEmpty()) {
            dimSelect = dimSelect + ",\n";
        }

        String dimGroupBy = dimensions.stream()
                .map(d -> String.format("JSON_VALUE(raw, '$.properties.%s')", d))
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
                  DATE_FORMAT(CURRENT_TIMESTAMP, 'yyyy-MM-dd') AS dt,
                  JSON_VALUE(raw, '$.appCode') AS app_code,
                  %s
                  %s
                FROM kafka_source
                WHERE JSON_VALUE(raw, '$.eventCode') = '%s'
                GROUP BY DATE_FORMAT(CURRENT_TIMESTAMP, 'yyyy-MM-dd'), JSON_VALUE(raw, '$.appCode')%s;
                
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
        return expr.replace("COUNT(DISTINCT user_id)", "COUNT(DISTINCT JSON_VALUE(raw, '$.userId'))")
                .replace("count(distinct user_id)", "COUNT(DISTINCT JSON_VALUE(raw, '$.userId'))");
    }
}

package com.cyan.datacollection.application.metric.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.Response;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.metric.TrackingMetricPipelineService;
import com.cyan.datacollection.application.metric.bo.TrackingMetricPipelineBO;
import com.cyan.datacollection.application.metric.cmd.TrackingMetricPipelineCmd;
import com.cyan.datacollection.application.metric.convert.TrackingMetricPipelineAppConvert;
import com.cyan.datacollection.application.metric.generator.TrackingMetricFlinkSqlGenerator;
import com.cyan.datacollection.domain.metric.TrackingMetricPipeline;
import com.cyan.datacollection.domain.metric.query.TrackingMetricPipelinePageQuery;
import com.cyan.datacollection.domain.metric.repository.TrackingMetricPipelineRepository;
import com.cyan.datacollection.enums.MetricPipelineStatus;
import com.cyan.dataworks.client.job.DataWorksRpcJobClient;
import com.cyan.dataworks.client.job.dto.JobDTO;
import com.cyan.dataworks.client.job.request.JobSaveRequest;
import com.cyan.dataworks.client.job_instance.DataWorksRpcJobInstanceClient;
import com.cyan.dataworks.client.job_instance.dto.JobInstanceDTO;
import com.cyan.dataman.client.table.MetadataTableClient;
import com.cyan.dataman.client.table.dto.MetadataTableDTO;
import com.cyan.dataman.client.table.request.MetadataColumnCreateRequest;
import com.cyan.dataman.client.table.request.MetadataTableCreateRequest;
import com.cyan.dataworks.client.enums.EngineType;
import com.cyan.dataworks.client.enums.NodeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 采集指标链路服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingMetricPipelineServiceImpl implements TrackingMetricPipelineService {

    private final TrackingMetricPipelineRepository repository;
    private final MetadataTableClient metadataTableClient;
    private final DataWorksRpcJobClient dataWorksRpcJobClient;
    private final DataWorksRpcJobInstanceClient dataWorksRpcJobInstanceClient;
    private final TrackingMetricFlinkSqlGenerator flinkSqlGenerator;

    public TrackingMetricPipelineServiceImpl(TrackingMetricPipelineRepository repository,
                                             MetadataTableClient metadataTableClient,
                                             DataWorksRpcJobClient dataWorksRpcJobClient,
                                             DataWorksRpcJobInstanceClient dataWorksRpcJobInstanceClient,
                                             TrackingMetricFlinkSqlGenerator flinkSqlGenerator) {
        this.repository = repository;
        this.metadataTableClient = metadataTableClient;
        this.dataWorksRpcJobClient = dataWorksRpcJobClient;
        this.dataWorksRpcJobInstanceClient = dataWorksRpcJobInstanceClient;
        this.flinkSqlGenerator = flinkSqlGenerator;
    }

    @Override
    public Page<TrackingMetricPipelineBO> page(TrackingMetricPipelinePageQuery query) {
        Page<TrackingMetricPipeline> page = repository.page(query);
        List<TrackingMetricPipelineBO> list = page.getData().stream()
                .map(TrackingMetricPipelineAppConvert.INSTANCE::toTrackingMetricPipelineBO)
                .toList();
        return new Page<>(list, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    @Transactional
    public TrackingMetricPipelineBO create(TrackingMetricPipelineCmd cmd, String createdBy) {
        TrackingMetricPipeline existing = repository.findByMetricCode(cmd.getMetricCode());
        Assert.isNull(existing, new SilentException("指标编码已存在: " + cmd.getMetricCode()));

        String metricCode = cmd.getMetricCode();
        TrackingMetricPipeline pipeline = TrackingMetricPipelineAppConvert.INSTANCE.toTrackingMetricPipeline(cmd);
        pipeline.setTopicName("cyan_data_collection_event_raw");
        pipeline.setOdsTableName("ods_tracking_event_raw_" + metricCode);
        pipeline.setDwdTableName("dwd_tracking_event_" + metricCode);
        pipeline.setDwsTableName("dws_tracking_metric_" + metricCode + "_di");
        pipeline.setAdsTableName("ads_tracking_metric_" + metricCode + "_di");
        pipeline.setDimensionsJson(JSON.toJSONString(Optional.ofNullable(cmd.getDimensions()).orElse(List.of())));
        List<TrackingMetricPipelineCmd.MeasureCmd> measures = Optional.ofNullable(cmd.getMeasures())
                .filter(list -> !list.isEmpty())
                .orElse(List.of(new TrackingMetricPipelineCmd.MeasureCmd("event_count", "COUNT(*)")));
        pipeline.setMeasuresJson(JSON.toJSONString(measures));
        pipeline.setCreateBy(createdBy);
        pipeline.setUpdateBy(createdBy);
        pipeline = pipeline.save(repository);
        return TrackingMetricPipelineAppConvert.INSTANCE.toTrackingMetricPipelineBO(pipeline);
    }

    @Override
    public TrackingMetricPipelineBO detail(String id) {
        TrackingMetricPipeline pipeline = repository.findById(id);
        Assert.notNull(pipeline, new SilentException("采集指标链路不存在"));
        return TrackingMetricPipelineAppConvert.INSTANCE.toTrackingMetricPipelineBO(pipeline);
    }

    @Override
    @Transactional
    public TrackingMetricPipelineBO provision(String id) {
        TrackingMetricPipeline pipeline = repository.findById(id);
        Assert.notNull(pipeline, new SilentException("采集指标链路不存在"));
        Assert.isTrue(pipeline.getStatus() == MetricPipelineStatus.DRAFT
                        || pipeline.getStatus() == MetricPipelineStatus.FAILED,
                new SilentException("当前状态不允许创建表"));

        List<String> dimensions = parseDimensions(pipeline);

        try {
            createTable(pipeline.getOdsTableName(), "ODS", buildOdsColumns(), pipeline);
            createTable(pipeline.getDwdTableName(), "DWD", buildDwdColumns(dimensions), pipeline);
            createTable(pipeline.getDwsTableName(), "DWS", buildDwsColumns(dimensions), pipeline);
            createTable(pipeline.getAdsTableName(), "ADS", buildAdsColumns(dimensions), pipeline);

            pipeline.setStatus(MetricPipelineStatus.TABLE_CREATED);
            pipeline.setErrorMessage(null);
        } catch (Exception e) {
            log.error("创建表失败, pipelineId={}", id, e);
            pipeline.setStatus(MetricPipelineStatus.FAILED);
            pipeline.setErrorMessage(e.getMessage());
        }

        pipeline = pipeline.update(repository);
        return TrackingMetricPipelineAppConvert.INSTANCE.toTrackingMetricPipelineBO(pipeline);
    }

    @Override
    @Transactional
    public TrackingMetricPipelineBO start(String id) {
        TrackingMetricPipeline pipeline = repository.findById(id);
        Assert.notNull(pipeline, new SilentException("采集指标链路不存在"));
        Assert.isTrue(pipeline.getStatus() == MetricPipelineStatus.TABLE_CREATED
                        || pipeline.getStatus() == MetricPipelineStatus.JOB_CREATED,
                new SilentException("当前状态不允许启动任务，请先创建表"));

        List<String> dimensions = parseDimensions(pipeline);
        List<TrackingMetricPipelineCmd.MeasureCmd> measures = parseMeasures(pipeline);

        try {
            String flinkSql = flinkSqlGenerator.generate(pipeline, dimensions, measures);

            // 1. 创建 DataWorks Job
            String jobName = "tracking_" + pipeline.getMetricCode() + "_pipeline";
            JobSaveRequest jobRequest = new JobSaveRequest()
                    .setName(jobName)
                    .setDescription("采集指标 " + pipeline.getMetricCode() + " 的 ODS-DWD-DWS-ADS FlinkSQL 任务")
                    .setEngineType(EngineType.FLINK)
                    .setNodeType(NodeType.FLINK_SQL)
                    .setSqlContent(flinkSql)
                    .setConfigJson("{\"source\":\"DATA_COLLECTION\",\"metricCode\":\"" + pipeline.getMetricCode() + "\"}");

            Response<JobDTO> jobResponse = dataWorksRpcJobClient.save(jobRequest);
            Assert.notNull(jobResponse, new SilentException("创建 DataWorks Job 失败: 返回为空"));
            Assert.notNull(jobResponse.getData(), new SilentException("创建 DataWorks Job 失败: " + jobResponse.getMessage()));
            String jobId = jobResponse.getData().getId();
            log.info("DataWorks Job 创建成功, jobId={}", jobId);

            // 2. 发布 Job
            Response<JobDTO> publishResponse = dataWorksRpcJobClient.publish(jobId);
            Assert.notNull(publishResponse, new SilentException("发布 DataWorks Job 失败: 返回为空"));
            Assert.notNull(publishResponse.getData(), new SilentException("发布 DataWorks Job 失败: " + publishResponse.getMessage()));
            log.info("DataWorks Job 发布成功, jobId={}", jobId);

            // 3. 启动 Application Mode
            Response<JobInstanceDTO> startResponse = dataWorksRpcJobInstanceClient.startApplication(jobId, "system");
            Assert.notNull(startResponse, new SilentException("启动 DataWorks Job 失败: 返回为空"));
            Assert.notNull(startResponse.getData(), new SilentException("启动 DataWorks Job 失败: " + startResponse.getMessage()));
            String instanceId = startResponse.getData().getId();
            log.info("DataWorks Job 启动成功, instanceId={}", instanceId);

            pipeline.setDataworksJobId(jobId);
            pipeline.setDataworksInstanceId(instanceId);
            pipeline.setFlinkDeploymentName(resolveDeploymentName(startResponse.getData(), pipeline));
            pipeline.setStatus(MetricPipelineStatus.RUNNING);
            pipeline.setErrorMessage(null);
        } catch (Exception e) {
            log.error("启动任务失败, pipelineId={}", id, e);
            pipeline.setStatus(MetricPipelineStatus.FAILED);
            pipeline.setErrorMessage(e.getMessage());
        }

        pipeline = pipeline.update(repository);
        return TrackingMetricPipelineAppConvert.INSTANCE.toTrackingMetricPipelineBO(pipeline);
    }

    private void createTable(String tableName, String layerCode, List<MetadataColumnCreateRequest> columns, TrackingMetricPipeline pipeline) {
        MetadataTableCreateRequest request = new MetadataTableCreateRequest()
                .setName(tableName)
                .setOwner("system")
                .setSubjectCode("data_collection")
                .setLayerCode(layerCode)
                .setComment("采集指标 " + pipeline.getMetricCode() + " " + layerCode + " 表")
                .setSecretLevel("L1")
                .setOnlineStatus("ONLINE")
                .setColumns(columns);

        Response<MetadataTableDTO> response = metadataTableClient.save(request);
        if (response == null || response.getData() == null) {
            throw new SilentException("创建表失败: " + tableName + ", msg=" + (response != null ? response.getMessage() : "null"));
        }
        log.info("创建表成功: {}.{}", layerCode.toLowerCase(), tableName);
    }

    private List<String> parseDimensions(TrackingMetricPipeline pipeline) {
        if (pipeline.getDimensionsJson() != null && !pipeline.getDimensionsJson().isBlank()) {
            return JSON.parseArray(pipeline.getDimensionsJson(), String.class);
        }
        return List.of();
    }

    private List<TrackingMetricPipelineCmd.MeasureCmd> parseMeasures(TrackingMetricPipeline pipeline) {
        if (pipeline.getMeasuresJson() != null && !pipeline.getMeasuresJson().isBlank()) {
            return JSON.parseObject(pipeline.getMeasuresJson(),
                    new TypeReference<List<TrackingMetricPipelineCmd.MeasureCmd>>() {
                    });
        }
        return List.of();
    }

    private String resolveDeploymentName(JobInstanceDTO instanceDTO, TrackingMetricPipeline pipeline) {
        String resultData = instanceDTO.getResultData();
        if (resultData != null && !resultData.isBlank()) {
            try {
                String deploymentName = JSON.parseObject(resultData).getString("deploymentName");
                if (deploymentName != null && !deploymentName.isBlank()) {
                    return deploymentName;
                }
            } catch (Exception e) {
                log.warn("解析 FlinkDeployment 名称失败, instanceId={}", instanceDTO.getId(), e);
            }
        }
        return "tracking-" + pipeline.getMetricCode().replace("_", "-") + "-pipeline";
    }

    private List<MetadataColumnCreateRequest> buildOdsColumns() {
        List<MetadataColumnCreateRequest> columns = new ArrayList<>();
        columns.add(new MetadataColumnCreateRequest("request_id", "STRING", "请求ID", true));
        columns.add(new MetadataColumnCreateRequest("app_code", "STRING", "应用编码", true));
        columns.add(new MetadataColumnCreateRequest("event_code", "STRING", "事件编码", true));
        columns.add(new MetadataColumnCreateRequest("event_time", "TIMESTAMP", "事件时间", true));
        columns.add(new MetadataColumnCreateRequest("ingestion_time", "TIMESTAMP", "采集时间", true));
        columns.add(new MetadataColumnCreateRequest("terminal_type", "STRING", "终端类型", true));
        columns.add(new MetadataColumnCreateRequest("environment", "STRING", "环境", true));
        columns.add(new MetadataColumnCreateRequest("user_id", "STRING", "用户ID", true));
        columns.add(new MetadataColumnCreateRequest("anonymous_id", "STRING", "匿名ID", true));
        columns.add(new MetadataColumnCreateRequest("session_id", "STRING", "会话ID", true));
        columns.add(new MetadataColumnCreateRequest("device_id", "STRING", "设备ID", true));
        columns.add(new MetadataColumnCreateRequest("page_code", "STRING", "页面编码", true));
        columns.add(new MetadataColumnCreateRequest("debug_token", "STRING", "调试Token", true));
        columns.add(new MetadataColumnCreateRequest("validate_status", "STRING", "校验状态", true));
        columns.add(new MetadataColumnCreateRequest("validate_errors", "STRING", "校验错误", true));
        columns.add(new MetadataColumnCreateRequest("properties", "STRING", "属性JSON", true));
        columns.add(new MetadataColumnCreateRequest("payload", "STRING", "原始Payload", true));
        columns.add(new MetadataColumnCreateRequest("dt", "STRING", "日期分区", true));
        return columns;
    }

    private List<MetadataColumnCreateRequest> buildDwdColumns(List<String> dimensions) {
        List<MetadataColumnCreateRequest> columns = new ArrayList<>();
        columns.add(new MetadataColumnCreateRequest("request_id", "STRING", "请求ID", true));
        columns.add(new MetadataColumnCreateRequest("app_code", "STRING", "应用编码", true));
        columns.add(new MetadataColumnCreateRequest("event_code", "STRING", "事件编码", true));
        columns.add(new MetadataColumnCreateRequest("event_time", "TIMESTAMP", "事件时间", true));
        columns.add(new MetadataColumnCreateRequest("user_id", "STRING", "用户ID", true));
        columns.add(new MetadataColumnCreateRequest("anonymous_id", "STRING", "匿名ID", true));
        columns.add(new MetadataColumnCreateRequest("session_id", "STRING", "会话ID", true));
        for (String dim : dimensions) {
            columns.add(new MetadataColumnCreateRequest(dim, "STRING", dim, true));
        }
        columns.add(new MetadataColumnCreateRequest("dt", "STRING", "日期分区", true));
        return columns;
    }

    private List<MetadataColumnCreateRequest> buildDwsColumns(List<String> dimensions) {
        List<MetadataColumnCreateRequest> columns = new ArrayList<>();
        columns.add(new MetadataColumnCreateRequest("dt", "STRING", "日期分区", true));
        columns.add(new MetadataColumnCreateRequest("app_code", "STRING", "应用编码", true));
        for (String dim : dimensions) {
            columns.add(new MetadataColumnCreateRequest(dim, "STRING", dim, true));
        }
        // measures 在 DWS 中硬编码为 click_count 和 user_count
        columns.add(new MetadataColumnCreateRequest("click_count", "BIGINT", "点击次数", true));
        columns.add(new MetadataColumnCreateRequest("user_count", "BIGINT", "用户数", true));
        return columns;
    }

    private List<MetadataColumnCreateRequest> buildAdsColumns(List<String> dimensions) {
        // 第一阶段 ADS 和 DWS 字段一致
        return buildDwsColumns(dimensions);
    }
}

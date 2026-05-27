package com.cyan.datacollection.infra.persistence.collect.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyan.datacollection.domain.collect.TrackingEventSample;
import com.cyan.datacollection.domain.collect.query.TrackingEventSamplePageQuery;
import com.cyan.datacollection.domain.collect.repository.TrackingEventSampleRepository;
import com.cyan.datacollection.infra.persistence.collect.convert.TrackingEventSampleInfraConvert;
import com.cyan.datacollection.infra.persistence.collect.dos.TrackingEventSampleDO;
import com.cyan.datacollection.infra.persistence.collect.mappers.TrackingEventSampleMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * 事件样本仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingEventSampleRepositoryImpl implements TrackingEventSampleRepository {

    private final TrackingEventSampleMapper trackingEventSampleMapper;

    public TrackingEventSampleRepositoryImpl(TrackingEventSampleMapper trackingEventSampleMapper) {
        this.trackingEventSampleMapper = trackingEventSampleMapper;
    }

    @Override
    public TrackingEventSample findById(String id) {
        TrackingEventSampleDO trackingEventSampleDO = trackingEventSampleMapper.selectById(Long.parseLong(id));
        return TrackingEventSampleInfraConvert.INSTANCE.toTrackingEventSample(trackingEventSampleDO);
    }

    @Override
    public com.cyan.arch.common.api.Page<TrackingEventSample> page(TrackingEventSamplePageQuery query) {
        Page<TrackingEventSampleDO> page = new Page<>(query.current(), query.size());
        LambdaQueryWrapper<TrackingEventSampleDO> wrapper = new LambdaQueryWrapper<TrackingEventSampleDO>()
                .eq(StringUtils.isNotBlank(query.getDebugToken()), TrackingEventSampleDO::getDebugToken, query.getDebugToken())
                .eq(StringUtils.isNotBlank(query.getAppCode()), TrackingEventSampleDO::getAppCode, query.getAppCode())
                .eq(StringUtils.isNotBlank(query.getEventCode()), TrackingEventSampleDO::getEventCode, query.getEventCode())
                .apply(StringUtils.isNotBlank(query.getEnvironment()),
                        "JSON_UNQUOTE(JSON_EXTRACT(common, '$.environment')) = {0}", query.getEnvironment())
                .apply(StringUtils.isNotBlank(query.getEmployeeId()),
                        "JSON_UNQUOTE(JSON_EXTRACT(business, '$.employee_id')) = {0}", query.getEmployeeId())
                .apply(StringUtils.isNotBlank(query.getAnonymousId()),
                        "JSON_UNQUOTE(JSON_EXTRACT(common, '$.anonymous_id')) = {0}", query.getAnonymousId())
                .apply(StringUtils.isNotBlank(query.getDeviceId()),
                        "JSON_UNQUOTE(JSON_EXTRACT(common, '$.device_id')) = {0}", query.getDeviceId())
                .ge(parseDateTime(query.getStartTime()) != null, TrackingEventSampleDO::getEventTime, parseDateTime(query.getStartTime()))
                .le(parseDateTime(query.getEndTime()) != null, TrackingEventSampleDO::getEventTime, parseDateTime(query.getEndTime()))
                .orderByDesc(TrackingEventSampleDO::getCreatedAt);
        Page<TrackingEventSampleDO> result = trackingEventSampleMapper.selectPage(page, wrapper);
        List<TrackingEventSample> list = Optional.ofNullable(result.getRecords()).orElse(List.of()).stream()
                .map(TrackingEventSampleInfraConvert.INSTANCE::toTrackingEventSample)
                .toList();
        return new com.cyan.arch.common.api.Page<>(list, result.getCurrent(), result.getSize(), result.getTotal());
    }

    @Override
    public TrackingEventSample save(TrackingEventSample sample) {
        TrackingEventSampleDO trackingEventSampleDO = TrackingEventSampleInfraConvert.INSTANCE.toTrackingEventSampleDO(sample);
        trackingEventSampleDO.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        trackingEventSampleDO.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        trackingEventSampleMapper.insert(trackingEventSampleDO);
        return findById(String.valueOf(trackingEventSampleDO.getId()));
    }

    @Override
    public List<TrackingEventSample> findByDebugToken(String debugToken) {
        LambdaQueryWrapper<TrackingEventSampleDO> wrapper = new LambdaQueryWrapper<TrackingEventSampleDO>()
                .eq(TrackingEventSampleDO::getDebugToken, debugToken)
                .orderByDesc(TrackingEventSampleDO::getCreatedAt);
        List<TrackingEventSampleDO> dos = trackingEventSampleMapper.selectList(wrapper);
        return Optional.ofNullable(dos).orElse(List.of()).stream()
                .map(TrackingEventSampleInfraConvert.INSTANCE::toTrackingEventSample)
                .toList();
    }

    @Override
    public List<TrackingEventSample> saveBatch(List<TrackingEventSample> samples) {
        for (TrackingEventSample sample : samples) {
            TrackingEventSampleDO trackingEventSampleDO = TrackingEventSampleInfraConvert.INSTANCE.toTrackingEventSampleDO(sample);
            trackingEventSampleDO.setCreatedAt(LocalDateTime.now());
            trackingEventSampleDO.setUpdatedAt(LocalDateTime.now());
            trackingEventSampleMapper.insert(trackingEventSampleDO);
            sample.setId(String.valueOf(trackingEventSampleDO.getId()));
        }
        return samples;
    }

    /**
     * 解析查询时间
     */
    private LocalDateTime parseDateTime(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException ignored) {
            try {
                return LocalDateTime.parse(value);
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }
}

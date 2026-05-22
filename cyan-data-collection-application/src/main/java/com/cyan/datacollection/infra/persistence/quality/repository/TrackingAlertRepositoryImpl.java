package com.cyan.datacollection.infra.persistence.quality.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyan.arch.common.api.Pageable;
import com.cyan.datacollection.domain.quality.TrackingAlert;
import com.cyan.datacollection.domain.quality.query.TrackingAlertPageQuery;
import com.cyan.datacollection.domain.quality.repository.TrackingAlertRepository;
import com.cyan.datacollection.enums.AlertLevel;
import com.cyan.datacollection.enums.AlertStatus;
import com.cyan.datacollection.enums.AlertType;
import com.cyan.datacollection.infra.persistence.quality.convert.TrackingQualityInfraConvert;
import com.cyan.datacollection.infra.persistence.quality.dos.TrackingAlertDO;
import com.cyan.datacollection.infra.persistence.quality.mappers.TrackingAlertMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 质量告警仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingAlertRepositoryImpl implements TrackingAlertRepository {

    private final TrackingAlertMapper trackingAlertMapper;

    public TrackingAlertRepositoryImpl(TrackingAlertMapper trackingAlertMapper) {
        this.trackingAlertMapper = trackingAlertMapper;
    }

    @Override
    public TrackingAlert findById(String id) {
        TrackingAlertDO dos = trackingAlertMapper.selectById(Long.parseLong(id));
        return TrackingQualityInfraConvert.INSTANCE.toDomain(dos);
    }

    @Override
    public com.cyan.arch.common.api.Page<TrackingAlert> page(TrackingAlertPageQuery query) {
        Page<TrackingAlertDO> page = new Page<>(query.current(), query.size());
        LambdaQueryWrapper<TrackingAlertDO> wrapper = new LambdaQueryWrapper<TrackingAlertDO>()
                .eq(StringUtils.isNotBlank(query.getAppCode()), TrackingAlertDO::getAppCode, query.getAppCode())
                .eq(StringUtils.isNotBlank(query.getEventCode()), TrackingAlertDO::getEventCode, query.getEventCode())
                .eq(StringUtils.isNotBlank(query.getAlertType()), TrackingAlertDO::getAlertType, AlertType.of(query.getAlertType()))
                .eq(StringUtils.isNotBlank(query.getAlertLevel()), TrackingAlertDO::getAlertLevel, AlertLevel.of(query.getAlertLevel()))
                .eq(StringUtils.isNotBlank(query.getStatus()), TrackingAlertDO::getStatus, AlertStatus.of(query.getStatus()))
                .orderByDesc(TrackingAlertDO::getTriggeredAt);
        Page<TrackingAlertDO> result = trackingAlertMapper.selectPage(page, wrapper);
        List<TrackingAlert> list = Optional.ofNullable(result.getRecords()).orElse(List.of()).stream()
                .map(TrackingQualityInfraConvert.INSTANCE::toDomain)
                .toList();
        return new com.cyan.arch.common.api.Page<>(list, result.getCurrent(), result.getSize(), result.getTotal());
    }

    @Override
    public TrackingAlert save(TrackingAlert alert) {
        TrackingAlertDO dos = TrackingQualityInfraConvert.INSTANCE.toDO(alert);
        trackingAlertMapper.insert(dos);
        return findById(String.valueOf(dos.getId()));
    }

    @Override
    public TrackingAlert update(TrackingAlert alert) {
        TrackingAlertDO dos = TrackingQualityInfraConvert.INSTANCE.toDO(alert);
        dos.setId(Long.parseLong(alert.getId()));
        trackingAlertMapper.updateById(dos);
        return findById(alert.getId());
    }

    @Override
    public void deleteById(String id) {
        trackingAlertMapper.deleteById(Long.parseLong(id));
    }

    @Override
    public List<TrackingAlert> findOpenByAppCodeAndEventCodeAndType(String appCode, String eventCode, AlertType alertType) {
        LambdaQueryWrapper<TrackingAlertDO> wrapper = new LambdaQueryWrapper<TrackingAlertDO>()
                .eq(TrackingAlertDO::getAppCode, appCode)
                .eq(TrackingAlertDO::getEventCode, eventCode)
                .eq(TrackingAlertDO::getAlertType, alertType)
                .eq(TrackingAlertDO::getStatus, AlertStatus.OPEN)
                .orderByDesc(TrackingAlertDO::getTriggeredAt);
        List<TrackingAlertDO> dos = trackingAlertMapper.selectList(wrapper);
        return Optional.ofNullable(dos).orElse(List.of()).stream()
                .map(TrackingQualityInfraConvert.INSTANCE::toDomain)
                .toList();
    }

    @Override
    public long countByStatus(AlertStatus status) {
        LambdaQueryWrapper<TrackingAlertDO> wrapper = new LambdaQueryWrapper<TrackingAlertDO>()
                .eq(TrackingAlertDO::getStatus, status);
        return Optional.ofNullable(trackingAlertMapper.selectCount(wrapper)).orElse(0L);
    }

    @Override
    public List<TrackingAlert> findOpenByLevel(String alertLevel) {
        LambdaQueryWrapper<TrackingAlertDO> wrapper = new LambdaQueryWrapper<TrackingAlertDO>()
                .eq(TrackingAlertDO::getAlertLevel, AlertLevel.of(alertLevel))
                .eq(TrackingAlertDO::getStatus, AlertStatus.OPEN)
                .orderByDesc(TrackingAlertDO::getTriggeredAt);
        List<TrackingAlertDO> dos = trackingAlertMapper.selectList(wrapper);
        return Optional.ofNullable(dos).orElse(List.of()).stream()
                .map(TrackingQualityInfraConvert.INSTANCE::toDomain)
                .toList();
    }
}

package com.cyan.datacollection.infra.persistence.app.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyan.arch.common.api.Pageable;
import com.cyan.datacollection.domain.app.TrackingApp;
import com.cyan.datacollection.domain.app.query.TrackingAppPageQuery;
import com.cyan.datacollection.domain.app.repository.TrackingAppRepository;
import com.cyan.datacollection.enums.AppStatus;
import com.cyan.datacollection.infra.persistence.app.convert.TrackingAppInfraConvert;
import com.cyan.datacollection.infra.persistence.app.dos.TrackingAppDO;
import com.cyan.datacollection.infra.persistence.app.mappers.TrackingAppMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 接入应用仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingAppRepositoryImpl implements TrackingAppRepository {

    private final TrackingAppMapper trackingAppMapper;

    public TrackingAppRepositoryImpl(TrackingAppMapper trackingAppMapper) {
        this.trackingAppMapper = trackingAppMapper;
    }

    @Override
    public TrackingApp findById(String id) {
        TrackingAppDO trackingAppDO = trackingAppMapper.selectById(Long.parseLong(id));
        return TrackingAppInfraConvert.INSTANCE.toTrackingApp(trackingAppDO);
    }

    @Override
    public com.cyan.arch.common.api.Page<TrackingApp> page(TrackingAppPageQuery query) {
        Page<TrackingAppDO> page = new Page<>(query.current(), query.size());
        LambdaQueryWrapper<TrackingAppDO> wrapper = new LambdaQueryWrapper<TrackingAppDO>()
                .like(StringUtils.isNotBlank(query.getAppCode()), TrackingAppDO::getAppCode, query.getAppCode())
                .like(StringUtils.isNotBlank(query.getAppName()), TrackingAppDO::getAppName, query.getAppName())
                .eq(StringUtils.isNotBlank(query.getAppType()), TrackingAppDO::getAppType, query.getAppType())
                .eq(StringUtils.isNotBlank(query.getStatus()), TrackingAppDO::getStatus, AppStatus.of(query.getStatus()))
                .orderByDesc(TrackingAppDO::getUpdatedAt);
        Page<TrackingAppDO> result = trackingAppMapper.selectPage(page, wrapper);
        List<TrackingApp> list = Optional.ofNullable(result.getRecords()).orElse(List.of()).stream()
                .map(TrackingAppInfraConvert.INSTANCE::toTrackingApp)
                .toList();
        return new com.cyan.arch.common.api.Page<>(list, result.getCurrent(), result.getSize(), result.getTotal());
    }

    @Override
    public TrackingApp findByCode(String appCode) {
        LambdaQueryWrapper<TrackingAppDO> wrapper = new LambdaQueryWrapper<TrackingAppDO>()
                .eq(TrackingAppDO::getAppCode, appCode);
        TrackingAppDO trackingAppDO = trackingAppMapper.selectOne(wrapper);
        return TrackingAppInfraConvert.INSTANCE.toTrackingApp(trackingAppDO);
    }

    @Override
    public TrackingApp save(TrackingApp app) {
        TrackingAppDO trackingAppDO = TrackingAppInfraConvert.INSTANCE.toTrackingAppDO(app);
        trackingAppMapper.insert(trackingAppDO);
        return findById(String.valueOf(trackingAppDO.getId()));
    }

    @Override
    public TrackingApp update(TrackingApp app) {
        TrackingAppDO trackingAppDO = TrackingAppInfraConvert.INSTANCE.toTrackingAppDO(app);
        trackingAppDO.setId(Long.parseLong(app.getId()));
        trackingAppMapper.updateById(trackingAppDO);
        return findById(app.getId());
    }

    @Override
    public void deleteById(String id) {
        trackingAppMapper.deleteById(Long.parseLong(id));
    }
}

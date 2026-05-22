package com.cyan.datacollection.infra.persistence.demand.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyan.arch.common.api.Pageable;
import com.cyan.datacollection.domain.demand.TrackingDemand;
import com.cyan.datacollection.domain.demand.query.TrackingDemandPageQuery;
import com.cyan.datacollection.domain.demand.repository.TrackingDemandRepository;
import com.cyan.datacollection.enums.DemandStatus;
import com.cyan.datacollection.infra.persistence.demand.convert.TrackingDemandInfraConvert;
import com.cyan.datacollection.infra.persistence.demand.dos.TrackingDemandDO;
import com.cyan.datacollection.infra.persistence.demand.mappers.TrackingDemandMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 埋点需求仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingDemandRepositoryImpl implements TrackingDemandRepository {

    private final TrackingDemandMapper trackingDemandMapper;

    public TrackingDemandRepositoryImpl(TrackingDemandMapper trackingDemandMapper) {
        this.trackingDemandMapper = trackingDemandMapper;
    }

    @Override
    public TrackingDemand findById(String id) {
        TrackingDemandDO trackingDemandDO = trackingDemandMapper.selectById(Long.parseLong(id));
        return TrackingDemandInfraConvert.INSTANCE.toTrackingDemand(trackingDemandDO);
    }

    @Override
    public com.cyan.arch.common.api.Page<TrackingDemand> page(TrackingDemandPageQuery query) {
        Page<TrackingDemandDO> page = new Page<>(query.current(), query.size());
        LambdaQueryWrapper<TrackingDemandDO> wrapper = new LambdaQueryWrapper<TrackingDemandDO>()
                .like(StringUtils.isNotBlank(query.getDemandCode()), TrackingDemandDO::getDemandCode, query.getDemandCode())
                .like(StringUtils.isNotBlank(query.getDemandName()), TrackingDemandDO::getDemandName, query.getDemandName())
                .eq(StringUtils.isNotBlank(query.getBusinessDomain()), TrackingDemandDO::getBusinessDomain, query.getBusinessDomain())
                .eq(StringUtils.isNotBlank(query.getPriority()), TrackingDemandDO::getPriority, query.getPriority())
                .eq(StringUtils.isNotBlank(query.getStatus()), TrackingDemandDO::getStatus, DemandStatus.of(query.getStatus()))
                .orderByDesc(TrackingDemandDO::getUpdatedAt);
        Page<TrackingDemandDO> result = trackingDemandMapper.selectPage(page, wrapper);
        List<TrackingDemand> list = Optional.ofNullable(result.getRecords()).orElse(List.of()).stream()
                .map(TrackingDemandInfraConvert.INSTANCE::toTrackingDemand)
                .toList();
        return new com.cyan.arch.common.api.Page<>(list, result.getCurrent(), result.getSize(), result.getTotal());
    }

    @Override
    public TrackingDemand findByCode(String demandCode) {
        LambdaQueryWrapper<TrackingDemandDO> wrapper = new LambdaQueryWrapper<TrackingDemandDO>()
                .eq(TrackingDemandDO::getDemandCode, demandCode);
        TrackingDemandDO trackingDemandDO = trackingDemandMapper.selectOne(wrapper);
        return TrackingDemandInfraConvert.INSTANCE.toTrackingDemand(trackingDemandDO);
    }

    @Override
    public TrackingDemand save(TrackingDemand demand) {
        TrackingDemandDO trackingDemandDO = TrackingDemandInfraConvert.INSTANCE.toTrackingDemandDO(demand);
        trackingDemandMapper.insert(trackingDemandDO);
        return findById(String.valueOf(trackingDemandDO.getId()));
    }

    @Override
    public TrackingDemand update(TrackingDemand demand) {
        TrackingDemandDO trackingDemandDO = TrackingDemandInfraConvert.INSTANCE.toTrackingDemandDO(demand);
        trackingDemandDO.setId(Long.parseLong(demand.getId()));
        trackingDemandMapper.updateById(trackingDemandDO);
        return findById(demand.getId());
    }

    @Override
    public void deleteById(String id) {
        trackingDemandMapper.deleteById(Long.parseLong(id));
    }

    @Override
    public int findMaxSeqToday() {
        return trackingDemandMapper.findMaxSeqToday();
    }
}

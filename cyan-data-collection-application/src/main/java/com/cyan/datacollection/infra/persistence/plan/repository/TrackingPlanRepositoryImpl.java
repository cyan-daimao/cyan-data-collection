package com.cyan.datacollection.infra.persistence.plan.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyan.arch.common.api.Pageable;
import com.cyan.datacollection.domain.plan.TrackingPlan;
import com.cyan.datacollection.domain.plan.query.TrackingPlanPageQuery;
import com.cyan.datacollection.domain.plan.repository.TrackingPlanRepository;
import com.cyan.datacollection.enums.PlanStatus;
import com.cyan.datacollection.infra.persistence.plan.convert.TrackingPlanInfraConvert;
import com.cyan.datacollection.infra.persistence.plan.dos.TrackingPlanDO;
import com.cyan.datacollection.infra.persistence.plan.mappers.TrackingPlanMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 埋点方案仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingPlanRepositoryImpl implements TrackingPlanRepository {

    private final TrackingPlanMapper trackingPlanMapper;

    public TrackingPlanRepositoryImpl(TrackingPlanMapper trackingPlanMapper) {
        this.trackingPlanMapper = trackingPlanMapper;
    }

    @Override
    public TrackingPlan findById(String id) {
        TrackingPlanDO dos = trackingPlanMapper.selectById(Long.parseLong(id));
        return TrackingPlanInfraConvert.INSTANCE.toDomain(dos);
    }

    @Override
    public com.cyan.arch.common.api.Page<TrackingPlan> page(TrackingPlanPageQuery query) {
        Page<TrackingPlanDO> page = new Page<>(query.current(), query.size());
        LambdaQueryWrapper<TrackingPlanDO> wrapper = new LambdaQueryWrapper<TrackingPlanDO>()
                .like(StringUtils.isNotBlank(query.getPlanCode()), TrackingPlanDO::getPlanCode, query.getPlanCode())
                .like(StringUtils.isNotBlank(query.getPlanName()), TrackingPlanDO::getPlanName, query.getPlanName())
                .eq(query.getDemandId() != null, TrackingPlanDO::getDemandId, query.getDemandId() != null ? Long.parseLong(query.getDemandId()) : null)
                .eq(StringUtils.isNotBlank(query.getStatus()), TrackingPlanDO::getStatus, PlanStatus.of(query.getStatus()))
                .orderByDesc(TrackingPlanDO::getUpdatedAt);
        Page<TrackingPlanDO> result = trackingPlanMapper.selectPage(page, wrapper);
        List<TrackingPlan> list = Optional.ofNullable(result.getRecords()).orElse(List.of()).stream()
                .map(TrackingPlanInfraConvert.INSTANCE::toDomain)
                .toList();
        return new com.cyan.arch.common.api.Page<>(list, result.getCurrent(), result.getSize(), result.getTotal());
    }

    @Override
    public TrackingPlan findByCode(String planCode) {
        LambdaQueryWrapper<TrackingPlanDO> wrapper = new LambdaQueryWrapper<TrackingPlanDO>()
                .eq(TrackingPlanDO::getPlanCode, planCode);
        TrackingPlanDO dos = trackingPlanMapper.selectOne(wrapper);
        return TrackingPlanInfraConvert.INSTANCE.toDomain(dos);
    }

    @Override
    public TrackingPlan save(TrackingPlan plan) {
        TrackingPlanDO dos = TrackingPlanInfraConvert.INSTANCE.toDO(plan);
        trackingPlanMapper.insert(dos);
        return findById(String.valueOf(dos.getId()));
    }

    @Override
    public TrackingPlan update(TrackingPlan plan) {
        TrackingPlanDO dos = TrackingPlanInfraConvert.INSTANCE.toDO(plan);
        dos.setId(Long.parseLong(plan.getId()));
        trackingPlanMapper.updateById(dos);
        return findById(plan.getId());
    }

    @Override
    public void deleteById(String id) {
        trackingPlanMapper.deleteById(Long.parseLong(id));
    }

    @Override
    public int findMaxSeqToday() {
        return trackingPlanMapper.findMaxSeqToday();
    }
}

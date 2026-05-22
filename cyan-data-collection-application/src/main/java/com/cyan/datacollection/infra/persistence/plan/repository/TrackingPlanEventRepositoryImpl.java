package com.cyan.datacollection.infra.persistence.plan.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyan.datacollection.domain.plan.TrackingPlanEventRelation;
import com.cyan.datacollection.domain.plan.repository.TrackingPlanEventRepository;
import com.cyan.datacollection.infra.persistence.plan.convert.TrackingPlanEventInfraConvert;
import com.cyan.datacollection.infra.persistence.plan.dos.TrackingPlanEventDO;
import com.cyan.datacollection.infra.persistence.plan.mappers.TrackingPlanEventMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 方案事件关系仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingPlanEventRepositoryImpl implements TrackingPlanEventRepository {

    private final TrackingPlanEventMapper trackingPlanEventMapper;

    public TrackingPlanEventRepositoryImpl(TrackingPlanEventMapper trackingPlanEventMapper) {
        this.trackingPlanEventMapper = trackingPlanEventMapper;
    }

    @Override
    public List<TrackingPlanEventRelation> findByPlanId(String planId) {
        LambdaQueryWrapper<TrackingPlanEventDO> wrapper = new LambdaQueryWrapper<TrackingPlanEventDO>()
                .eq(TrackingPlanEventDO::getPlanId, Long.parseLong(planId));
        List<TrackingPlanEventDO> dos = trackingPlanEventMapper.selectList(wrapper);
        return Optional.ofNullable(dos).orElse(List.of()).stream()
                .map(TrackingPlanEventInfraConvert.INSTANCE::toRelation)
                .toList();
    }

    @Override
    public TrackingPlanEventRelation save(TrackingPlanEventRelation relation) {
        TrackingPlanEventDO dos = TrackingPlanEventInfraConvert.INSTANCE.toDO(relation);
        trackingPlanEventMapper.insert(dos);
        return findById(String.valueOf(dos.getId()));
    }

    @Override
    public void deleteByPlanIdAndEventId(String planId, String eventId) {
        LambdaQueryWrapper<TrackingPlanEventDO> wrapper = new LambdaQueryWrapper<TrackingPlanEventDO>()
                .eq(TrackingPlanEventDO::getPlanId, Long.parseLong(planId))
                .eq(TrackingPlanEventDO::getEventId, Long.parseLong(eventId));
        trackingPlanEventMapper.delete(wrapper);
    }

    @Override
    public boolean existsByPlanIdAndEventId(String planId, String eventId) {
        LambdaQueryWrapper<TrackingPlanEventDO> wrapper = new LambdaQueryWrapper<TrackingPlanEventDO>()
                .eq(TrackingPlanEventDO::getPlanId, Long.parseLong(planId))
                .eq(TrackingPlanEventDO::getEventId, Long.parseLong(eventId));
        return trackingPlanEventMapper.selectCount(wrapper) > 0;
    }

    /**
     * 根据ID查询
     */
    private TrackingPlanEventRelation findById(String id) {
        TrackingPlanEventDO dos = trackingPlanEventMapper.selectById(Long.parseLong(id));
        return TrackingPlanEventInfraConvert.INSTANCE.toRelation(dos);
    }
}

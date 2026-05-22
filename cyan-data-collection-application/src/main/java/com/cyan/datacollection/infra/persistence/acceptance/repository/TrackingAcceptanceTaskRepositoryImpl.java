package com.cyan.datacollection.infra.persistence.acceptance.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyan.arch.common.api.Pageable;
import com.cyan.arch.common.util.Convert;
import com.cyan.arch.common.util.StrUtils;
import com.cyan.datacollection.domain.acceptance.TrackingAcceptanceTask;
import com.cyan.datacollection.domain.acceptance.query.TrackingAcceptanceTaskPageQuery;
import com.cyan.datacollection.domain.acceptance.repository.TrackingAcceptanceTaskRepository;
import com.cyan.datacollection.enums.AcceptanceStatus;
import com.cyan.datacollection.infra.persistence.acceptance.convert.TrackingAcceptanceInfraConvert;
import com.cyan.datacollection.infra.persistence.acceptance.dos.TrackingAcceptanceTaskDO;
import com.cyan.datacollection.infra.persistence.acceptance.mappers.TrackingAcceptanceTaskMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 验收任务仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingAcceptanceTaskRepositoryImpl implements TrackingAcceptanceTaskRepository {

    private final TrackingAcceptanceTaskMapper trackingAcceptanceTaskMapper;

    public TrackingAcceptanceTaskRepositoryImpl(TrackingAcceptanceTaskMapper trackingAcceptanceTaskMapper) {
        this.trackingAcceptanceTaskMapper = trackingAcceptanceTaskMapper;
    }

    @Override
    public TrackingAcceptanceTask findById(String id) {
        TrackingAcceptanceTaskDO dos = trackingAcceptanceTaskMapper.selectById(Long.parseLong(id));
        return TrackingAcceptanceInfraConvert.INSTANCE.toTaskDomain(dos);
    }

    @Override
    public com.cyan.arch.common.api.Page<TrackingAcceptanceTask> page(TrackingAcceptanceTaskPageQuery query) {
        Page<TrackingAcceptanceTaskDO> page = new Page<>(query.current(), query.size());
        LambdaQueryWrapper<TrackingAcceptanceTaskDO> wrapper = new LambdaQueryWrapper<TrackingAcceptanceTaskDO>()
                .like(StringUtils.isNotBlank(query.getTaskCode()), TrackingAcceptanceTaskDO::getTaskCode, query.getTaskCode())
                .eq(StrUtils.isNotBlank(query.getPlanId()), TrackingAcceptanceTaskDO::getPlanId, query.getPlanId() != null ? Convert.toLong(query.getPlanId()) : null)
                .eq(StringUtils.isNotBlank(query.getStatus()), TrackingAcceptanceTaskDO::getStatus, AcceptanceStatus.of(query.getStatus()))
                .eq(StringUtils.isNotBlank(query.getDebugToken()), TrackingAcceptanceTaskDO::getDebugToken, query.getDebugToken())
                .orderByDesc(TrackingAcceptanceTaskDO::getUpdatedAt);
        Page<TrackingAcceptanceTaskDO> result = trackingAcceptanceTaskMapper.selectPage(page, wrapper);
        List<TrackingAcceptanceTask> list = Optional.ofNullable(result.getRecords()).orElse(List.of()).stream()
                .map(TrackingAcceptanceInfraConvert.INSTANCE::toTaskDomain)
                .toList();
        return new com.cyan.arch.common.api.Page<>(list, result.getCurrent(), result.getSize(), result.getTotal());
    }

    @Override
    public TrackingAcceptanceTask save(TrackingAcceptanceTask task) {
        TrackingAcceptanceTaskDO dos = TrackingAcceptanceInfraConvert.INSTANCE.toTaskDO(task);
        trackingAcceptanceTaskMapper.insert(dos);
        return findById(String.valueOf(dos.getId()));
    }

    @Override
    public TrackingAcceptanceTask update(TrackingAcceptanceTask task) {
        TrackingAcceptanceTaskDO dos = TrackingAcceptanceInfraConvert.INSTANCE.toTaskDO(task);
        dos.setId(Long.parseLong(task.getId()));
        trackingAcceptanceTaskMapper.updateById(dos);
        return findById(task.getId());
    }

    @Override
    public void deleteById(String id) {
        trackingAcceptanceTaskMapper.deleteById(Long.parseLong(id));
    }

    @Override
    public List<TrackingAcceptanceTask> findByPlanId(String planId) {
        LambdaQueryWrapper<TrackingAcceptanceTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrackingAcceptanceTaskDO::getPlanId, Long.parseLong(planId));
        wrapper.orderByDesc(TrackingAcceptanceTaskDO::getCreatedAt);
        List<TrackingAcceptanceTaskDO> dos = trackingAcceptanceTaskMapper.selectList(wrapper);
        return dos.stream().map(TrackingAcceptanceInfraConvert.INSTANCE::toTaskDomain).toList();
    }

    @Override
    public int findMaxSeqToday() {
        return trackingAcceptanceTaskMapper.findMaxSeqToday();
    }
}

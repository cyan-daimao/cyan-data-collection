package com.cyan.datacollection.infra.persistence.acceptance.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyan.datacollection.domain.acceptance.TrackingAcceptanceResult;
import com.cyan.datacollection.domain.acceptance.repository.TrackingAcceptanceResultRepository;
import com.cyan.datacollection.infra.persistence.acceptance.convert.TrackingAcceptanceInfraConvert;
import com.cyan.datacollection.infra.persistence.acceptance.dos.TrackingAcceptanceResultDO;
import com.cyan.datacollection.infra.persistence.acceptance.mappers.TrackingAcceptanceResultMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 验收结果仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingAcceptanceResultRepositoryImpl implements TrackingAcceptanceResultRepository {

    private final TrackingAcceptanceResultMapper trackingAcceptanceResultMapper;

    public TrackingAcceptanceResultRepositoryImpl(TrackingAcceptanceResultMapper trackingAcceptanceResultMapper) {
        this.trackingAcceptanceResultMapper = trackingAcceptanceResultMapper;
    }

    @Override
    public TrackingAcceptanceResult findById(String id) {
        TrackingAcceptanceResultDO dos = trackingAcceptanceResultMapper.selectById(Long.parseLong(id));
        return TrackingAcceptanceInfraConvert.INSTANCE.toResultDomain(dos);
    }

    @Override
    public List<TrackingAcceptanceResult> findByTaskId(String taskId) {
        LambdaQueryWrapper<TrackingAcceptanceResultDO> wrapper = new LambdaQueryWrapper<TrackingAcceptanceResultDO>()
                .eq(TrackingAcceptanceResultDO::getTaskId, Long.parseLong(taskId))
                .orderByAsc(TrackingAcceptanceResultDO::getCreatedAt);
        List<TrackingAcceptanceResultDO> dos = trackingAcceptanceResultMapper.selectList(wrapper);
        return Optional.ofNullable(dos).orElse(List.of()).stream()
                .map(TrackingAcceptanceInfraConvert.INSTANCE::toResultDomain)
                .toList();
    }

    @Override
    public TrackingAcceptanceResult save(TrackingAcceptanceResult result) {
        TrackingAcceptanceResultDO dos = TrackingAcceptanceInfraConvert.INSTANCE.toResultDO(result);
        trackingAcceptanceResultMapper.insert(dos);
        return findById(String.valueOf(dos.getId()));
    }

    @Override
    public List<TrackingAcceptanceResult> saveBatch(List<TrackingAcceptanceResult> results) {
        for (TrackingAcceptanceResult result : results) {
            TrackingAcceptanceResultDO dos = TrackingAcceptanceInfraConvert.INSTANCE.toResultDO(result);
            trackingAcceptanceResultMapper.insert(dos);
            result.setId(String.valueOf(dos.getId()));
        }
        return results;
    }

    @Override
    public void deleteByTaskId(String taskId) {
        LambdaQueryWrapper<TrackingAcceptanceResultDO> wrapper = new LambdaQueryWrapper<TrackingAcceptanceResultDO>()
                .eq(TrackingAcceptanceResultDO::getTaskId, Long.parseLong(taskId));
        trackingAcceptanceResultMapper.delete(wrapper);
    }
}

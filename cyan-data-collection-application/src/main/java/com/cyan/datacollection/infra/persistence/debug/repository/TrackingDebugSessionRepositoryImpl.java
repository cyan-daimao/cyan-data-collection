package com.cyan.datacollection.infra.persistence.debug.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyan.datacollection.domain.debug.TrackingDebugSession;
import com.cyan.datacollection.domain.debug.repository.TrackingDebugSessionRepository;
import com.cyan.datacollection.infra.persistence.debug.convert.TrackingDebugSessionInfraConvert;
import com.cyan.datacollection.infra.persistence.debug.dos.TrackingDebugSessionDO;
import com.cyan.datacollection.infra.persistence.debug.mappers.TrackingDebugSessionMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Debug 会话仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingDebugSessionRepositoryImpl implements TrackingDebugSessionRepository {

    private final TrackingDebugSessionMapper trackingDebugSessionMapper;

    public TrackingDebugSessionRepositoryImpl(TrackingDebugSessionMapper trackingDebugSessionMapper) {
        this.trackingDebugSessionMapper = trackingDebugSessionMapper;
    }

    @Override
    public TrackingDebugSession findById(String id) {
        TrackingDebugSessionDO trackingDebugSessionDO = trackingDebugSessionMapper.selectById(Long.parseLong(id));
        return TrackingDebugSessionInfraConvert.INSTANCE.toTrackingDebugSession(trackingDebugSessionDO);
    }

    @Override
    public TrackingDebugSession findByToken(String debugToken) {
        LambdaQueryWrapper<TrackingDebugSessionDO> wrapper = new LambdaQueryWrapper<TrackingDebugSessionDO>()
                .eq(TrackingDebugSessionDO::getDebugToken, debugToken);
        TrackingDebugSessionDO trackingDebugSessionDO = trackingDebugSessionMapper.selectOne(wrapper);
        return TrackingDebugSessionInfraConvert.INSTANCE.toTrackingDebugSession(trackingDebugSessionDO);
    }

    @Override
    public TrackingDebugSession save(TrackingDebugSession session) {
        TrackingDebugSessionDO trackingDebugSessionDO = TrackingDebugSessionInfraConvert.INSTANCE.toTrackingDebugSessionDO(session);
        trackingDebugSessionDO.setUpdatedAt(LocalDateTime.now());
        trackingDebugSessionMapper.insert(trackingDebugSessionDO);
        return findById(String.valueOf(trackingDebugSessionDO.getId()));
    }

    @Override
    public TrackingDebugSession update(TrackingDebugSession session) {
        TrackingDebugSessionDO trackingDebugSessionDO = TrackingDebugSessionInfraConvert.INSTANCE.toTrackingDebugSessionDO(session);
        trackingDebugSessionDO.setId(Long.parseLong(session.getId()));
        trackingDebugSessionDO.setUpdatedAt(LocalDateTime.now());
        trackingDebugSessionMapper.updateById(trackingDebugSessionDO);
        return findById(session.getId());
    }
}

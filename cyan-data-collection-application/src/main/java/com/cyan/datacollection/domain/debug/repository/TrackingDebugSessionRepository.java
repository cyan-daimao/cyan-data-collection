package com.cyan.datacollection.domain.debug.repository;

import com.cyan.datacollection.domain.debug.TrackingDebugSession;

/**
 * Debug 会话仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingDebugSessionRepository {

    TrackingDebugSession findById(String id);

    TrackingDebugSession findByToken(String debugToken);

    TrackingDebugSession save(TrackingDebugSession session);

    TrackingDebugSession update(TrackingDebugSession session);
}

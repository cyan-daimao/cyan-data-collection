package com.cyan.datacollection.application.debug;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.application.debug.bo.DebugEventSampleBO;
import com.cyan.datacollection.application.debug.bo.DebugSessionBO;
import com.cyan.datacollection.application.debug.cmd.DebugSessionCmd;
import com.cyan.datacollection.domain.collect.query.TrackingEventSamplePageQuery;

/**
 * Debug 控制台服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingDebugService {

    DebugSessionBO createSession(DebugSessionCmd cmd);

    DebugSessionBO sessionDetail(String id);

    Page<DebugEventSampleBO> eventPage(TrackingEventSamplePageQuery query);

    DebugEventSampleBO eventDetail(String id);
}

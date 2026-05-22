package com.cyan.datacollection.application.app;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.application.app.bo.TrackingAppBO;
import com.cyan.datacollection.application.app.cmd.TrackingAppCmd;
import com.cyan.datacollection.domain.app.query.TrackingAppPageQuery;

/**
 * 接入应用服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingAppService {

    Page<TrackingAppBO> page(TrackingAppPageQuery query);

    TrackingAppBO create(TrackingAppCmd cmd);

    TrackingAppBO update(String id, TrackingAppCmd cmd);

    TrackingAppBO detail(String id);

    TrackingAppBO rotateSecret(String id);

    TrackingAppBO.IntegrationBO integrationCode(String id);
}

package com.cyan.datacollection.application.demand;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.application.demand.bo.TrackingDemandBO;
import com.cyan.datacollection.application.demand.cmd.TrackingDemandCmd;
import com.cyan.datacollection.domain.demand.query.TrackingDemandPageQuery;

/**
 * 埋点需求服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingDemandService {

    Page<TrackingDemandBO> page(TrackingDemandPageQuery query);

    TrackingDemandBO create(TrackingDemandCmd cmd);

    TrackingDemandBO update(String id, TrackingDemandCmd cmd);

    TrackingDemandBO detail(String id);

    TrackingDemandBO submitDesign(String id);

    TrackingDemandBO close(String id);
}

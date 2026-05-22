package com.cyan.datacollection.domain.app.repository;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.domain.app.TrackingApp;
import com.cyan.datacollection.domain.app.query.TrackingAppPageQuery;

/**
 * 接入应用仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingAppRepository {

    TrackingApp findById(String id);

    Page<TrackingApp> page(TrackingAppPageQuery query);

    TrackingApp findByCode(String appCode);

    TrackingApp save(TrackingApp app);

    TrackingApp update(TrackingApp app);

    void deleteById(String id);
}

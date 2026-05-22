package com.cyan.datacollection.application.quality;

import com.cyan.datacollection.application.quality.bo.LinkStatusBO;

/**
 * 采集链路状态服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingLinkStatusService {

    /**
     * 获取链路状态概览
     */
    LinkStatusBO getLinkStatus();
}

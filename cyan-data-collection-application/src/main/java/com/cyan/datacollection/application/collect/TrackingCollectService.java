package com.cyan.datacollection.application.collect;

import com.cyan.datacollection.application.collect.bo.CollectResultBO;
import com.cyan.datacollection.application.collect.cmd.EventCollectCmd;

import java.util.List;

/**
 * 事件上报服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingCollectService {

    /**
     * 单条事件上报
     */
    CollectResultBO collect(EventCollectCmd cmd);

    /**
     * 批量事件上报
     */
    List<CollectResultBO> collectBatch(List<EventCollectCmd> cmds);
}

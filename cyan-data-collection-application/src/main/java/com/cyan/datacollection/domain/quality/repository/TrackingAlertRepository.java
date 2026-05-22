package com.cyan.datacollection.domain.quality.repository;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.domain.quality.TrackingAlert;
import com.cyan.datacollection.domain.quality.query.TrackingAlertPageQuery;
import com.cyan.datacollection.enums.AlertStatus;
import com.cyan.datacollection.enums.AlertType;

import java.util.List;

/**
 * 质量告警仓储
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingAlertRepository {

    /**
     * 根据ID查询
     */
    TrackingAlert findById(String id);

    /**
     * 分页查询
     */
    Page<TrackingAlert> page(TrackingAlertPageQuery query);

    /**
     * 保存
     */
    TrackingAlert save(TrackingAlert alert);

    /**
     * 更新
     */
    TrackingAlert update(TrackingAlert alert);

    /**
     * 根据ID删除
     */
    void deleteById(String id);

    /**
     * 查询指定事件未关闭的同类告警
     */
    List<TrackingAlert> findOpenByAppCodeAndEventCodeAndType(String appCode, String eventCode, AlertType alertType);

    /**
     * 按状态统计数量
     */
    long countByStatus(AlertStatus status);

    /**
     * 查询未关闭的指定级别告警列表
     */
    List<TrackingAlert> findOpenByLevel(String alertLevel);
}

package com.cyan.datacollection.application.release;

import com.cyan.arch.common.api.Page;
import com.cyan.datacollection.application.release.bo.TrackingReleaseBO;
import com.cyan.datacollection.application.release.cmd.TrackingReleaseCmd;
import com.cyan.datacollection.domain.release.query.TrackingReleasePageQuery;

import java.util.List;

/**
 * 埋点发布版本服务
 *
 * @author cy.Y
 * @since 1.0.0
 */
public interface TrackingReleaseService {

    /**
     * 分页查询
     */
    Page<TrackingReleaseBO> page(TrackingReleasePageQuery query);

    /**
     * 创建发布版本
     */
    TrackingReleaseBO create(TrackingReleaseCmd cmd);

    /**
     * 发布详情（包含明细列表）
     */
    TrackingReleaseBO detail(String id);

    /**
     * 发布diff（MVP返回当前发布明细列表）
     */
    List<TrackingReleaseBO.ItemBO> diff(String id);

    /**
     * 提交发布
     */
    TrackingReleaseBO submit(String id);

    /**
     * 发布上线
     */
    TrackingReleaseBO publish(String id);
}

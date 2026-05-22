package com.cyan.datacollection.infra.persistence.release.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.release.dos.TrackingReleaseDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 埋点发布版本 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingReleaseMapper extends BaseMapper<TrackingReleaseDO> {

    /**
     * 获取当天最大序号
     */
    @Select("SELECT IFNULL(MAX(CAST(SUBSTRING(release_code, 11) AS UNSIGNED)), 0) FROM tracking_release WHERE release_code LIKE CONCAT('RL', DATE_FORMAT(NOW(), '%Y%m%d'), '%') AND deleted_at IS NULL")
    int findMaxSeqToday();
}

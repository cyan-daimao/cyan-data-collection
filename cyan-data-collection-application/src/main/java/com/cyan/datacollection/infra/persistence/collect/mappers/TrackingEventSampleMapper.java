package com.cyan.datacollection.infra.persistence.collect.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.application.quality.bo.QualityOverviewBO;
import com.cyan.datacollection.application.quality.bo.QualityTrendBO;
import com.cyan.datacollection.infra.persistence.collect.dos.TrackingEventSampleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 事件样本 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingEventSampleMapper extends BaseMapper<TrackingEventSampleDO> {

    /**
     * 按应用和事件聚合今日质量指标
     */
    @Select("<script>"
            + "SELECT app_code as appCode, event_code as eventCode, "
            + "COUNT(*) as totalCount, "
            + "SUM(CASE WHEN validate_status = 'PASS' THEN 1 ELSE 0 END) as passCount, "
            + "SUM(CASE WHEN validate_status = 'WARN' THEN 1 ELSE 0 END) as warnCount, "
            + "SUM(CASE WHEN validate_status = 'FAIL' THEN 1 ELSE 0 END) as failCount "
            + "FROM tracking_event_sample "
            + "WHERE deleted_at IS NULL AND DATE(created_at) = CURDATE() "
            + "<if test='appCode != null and appCode != \"\"'> AND app_code = #{appCode} </if>"
            + "<if test='eventCode != null and eventCode != \"\"'> AND event_code = #{eventCode} </if>"
            + "GROUP BY app_code, event_code"
            + "</script>")
    List<QualityOverviewBO> aggregateToday(@Param("appCode") String appCode, @Param("eventCode") String eventCode);

    /**
     * 按小时统计质量趋势
     */
    @Select("<script>"
            + "SELECT DATE_FORMAT(created_at, '%Y-%m-%d %H:00') as timeHour, "
            + "SUM(CASE WHEN validate_status = 'PASS' THEN 1 ELSE 0 END) as passCount, "
            + "SUM(CASE WHEN validate_status = 'WARN' THEN 1 ELSE 0 END) as warnCount, "
            + "SUM(CASE WHEN validate_status = 'FAIL' THEN 1 ELSE 0 END) as failCount "
            + "FROM tracking_event_sample "
            + "WHERE deleted_at IS NULL "
            + "AND created_at &gt;= #{startTime} AND created_at &lt;= #{endTime} "
            + "<if test='appCode != null and appCode != \"\"'> AND app_code = #{appCode} </if>"
            + "<if test='eventCode != null and eventCode != \"\"'> AND event_code = #{eventCode} </if>"
            + "GROUP BY DATE_FORMAT(created_at, '%Y-%m-%d %H:00') "
            + "ORDER BY timeHour"
            + "</script>")
    List<QualityTrendBO> trendByHour(@Param("appCode") String appCode, @Param("eventCode") String eventCode,
                                     @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计最近时间段内某事件的样本数
     */
    @Select("SELECT COUNT(*) FROM tracking_event_sample "
            + "WHERE deleted_at IS NULL AND app_code = #{appCode} AND event_code = #{eventCode} "
            + "AND created_at &gt;= #{since}")
    Long countRecentByAppCodeAndEventCode(@Param("appCode") String appCode, @Param("eventCode") String eventCode,
                                          @Param("since") LocalDateTime since);

    /**
     * 统计今日样本总量
     */
    @Select("SELECT COUNT(*) FROM tracking_event_sample WHERE deleted_at IS NULL AND DATE(created_at) = CURDATE()")
    Long countToday();

    /**
     * 按状态统计今日样本数
     */
    @Select("SELECT COUNT(*) FROM tracking_event_sample WHERE deleted_at IS NULL AND DATE(created_at) = CURDATE() AND validate_status = #{status}")
    Long countTodayByStatus(@Param("status") String status);

    /**
     * 统计今日带 Debug Token 的样本数
     */
    @Select("SELECT COUNT(*) FROM tracking_event_sample WHERE deleted_at IS NULL AND DATE(created_at) = CURDATE() AND debug_token IS NOT NULL AND debug_token != ''")
    Long countTodayByDebugTokenNotNull();

    /**
     * 按时间范围聚合质量指标
     */
    @Select("<script>"
            + "SELECT app_code as appCode, event_code as eventCode, "
            + "COUNT(*) as totalCount, "
            + "SUM(CASE WHEN validate_status = 'PASS' THEN 1 ELSE 0 END) as passCount, "
            + "SUM(CASE WHEN validate_status = 'WARN' THEN 1 ELSE 0 END) as warnCount, "
            + "SUM(CASE WHEN validate_status = 'FAIL' THEN 1 ELSE 0 END) as failCount "
            + "FROM tracking_event_sample "
            + "WHERE deleted_at IS NULL "
            + "AND created_at &gt;= #{startTime} AND created_at &lt;= #{endTime} "
            + "<if test='appCode != null and appCode != \"\"'> AND app_code = #{appCode} </if>"
            + "<if test='eventCode != null and eventCode != \"\"'> AND event_code = #{eventCode} </if>"
            + "GROUP BY app_code, event_code"
            + "</script>")
    List<QualityOverviewBO> aggregateByTimeRange(@Param("appCode") String appCode, @Param("eventCode") String eventCode,
                                                  @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}

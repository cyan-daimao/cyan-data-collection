package com.cyan.datacollection.application.event.bo;

import com.cyan.datacollection.application.eventproperty.bo.EventPropertyBO;
import com.cyan.datacollection.enums.EventStatus;
import com.cyan.datacollection.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 事件定义业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingEventBO {

    /**
     * 主键
     */
    private String id;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件类型
     */
    private EventType eventType;

    /**
     * 业务域
     */
    private String businessDomain;

    /**
     * 业务描述
     */
    private String description;

    /**
     * 触发时机
     */
    private String triggerTiming;

    /**
     * 支持端类型
     */
    private List<String> terminalTypes;

    /**
     * 负责人
     */
    private String owner;

    /**
     * 是否核心事件
     */
    private Boolean isCore;

    /**
     * 状态
     */
    private EventStatus status;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 事件属性列表
     */
    private List<EventPropertyBO> properties;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class UsageBO {
        /**
         * 事件ID
         */
        private String eventId;

        /**
         * 方案数量
         */
        private Integer planCount;

        /**
         * 关联方案
         */
        private List<PlanRefBO> plans;

        /**
         * 近期样本数
         */
        private Long recentSampleCount;

        /**
         * 属性数量
         */
        private Integer propertyCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class PlanRefBO {
        /**
         * 方案ID
         */
        private String planId;

        /**
         * 方案编码
         */
        private String planCode;

        /**
         * 方案名称
         */
        private String planName;
    }
}

package com.cyan.datacollection.application.plan.bo;

import com.cyan.datacollection.enums.PlanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 埋点方案业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingPlanBO {

    /**
     * 主键
     */
    private String id;

    /**
     * 方案编号
     */
    private String planCode;

    /**
     * 方案名称
     */
    private String planName;

    /**
     * 关联需求ID
     */
    private String demandId;

    /**
     * 关联需求编号
     */
    private String demandCode;

    /**
     * 方案版本
     */
    private Integer version;

    /**
     * 方案描述
     */
    private String description;

    /**
     * 状态
     */
    private PlanStatus status;

    /**
     * 评审人
     */
    private String reviewer;

    /**
     * 已发布版本ID
     */
    private String publishedVersionId;

    /**
     * 方案内事件列表
     */
    private List<PlanEventBO> events;

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
     * 方案内事件业务对象
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class PlanEventBO {

        /**
         * 事件ID
         */
        private String eventId;

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
        private String eventType;

        /**
         * 在该方案中是否必填
         */
        private Boolean isRequired;
    }
}

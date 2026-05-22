package com.cyan.datacollection.application.acceptance.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 验收结果业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingAcceptanceResultBO {

    /**
     * 主键
     */
    private String id;

    /**
     * 验收任务ID
     */
    private String taskId;

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 状态: PASS, FAIL
     */
    private String status;

    /**
     * 错误项列表
     */
    private List<String> errorItems;

    /**
     * 命中的样本ID列表
     */
    private List<String> sampleIds;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}

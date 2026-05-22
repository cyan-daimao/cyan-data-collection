package com.cyan.datacollection.adapter.collect.controller.dto;

import com.cyan.datacollection.enums.ValidateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 事件上报结果DTO
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CollectResultDTO {

    /**
     * 是否接收成功
     */
    private Boolean accepted;

    /**
     * 样本ID
     */
    private String sampleId;

    /**
     * 校验状态
     */
    private ValidateStatus validateStatus;

    /**
     * 校验错误列表
     */
    private List<String> errors;
}

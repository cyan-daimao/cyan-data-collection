package com.cyan.datacollection.adapter.collect.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 事件上报请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EventCollectRequest {

    /**
     * 公共上下文
     */
    private Map<String, Object> common;

    /**
     * 行为信息
     */
    private Map<String, Object> action;

    /**
     * 业务字段
     */
    private Map<String, Object> business;

    /**
     * 额外扩展字段
     */
    private Map<String, Object> extra;
}

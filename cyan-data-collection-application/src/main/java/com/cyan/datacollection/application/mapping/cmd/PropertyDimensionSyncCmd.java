package com.cyan.datacollection.application.mapping.cmd;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 属性同步维度命令
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class PropertyDimensionSyncCmd {

    /**
     * 维度编码
     */
    private String dimCode;

    /**
     * 维度名称
     */
    private String dimName;

    /**
     * 维度类型
     */
    private String dimType;

    /**
     * 维度分类ID
     */
    private String categoryId;

    /**
     * 负责人
     */
    private String owner;

    /**
     * 操作人
     */
    private String operator;
}

package com.cyan.datacollection.application.mapping.bo;

import com.cyan.datacollection.enums.SyncStatus;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 属性维度映射业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class PropertyDimensionMappingBO {

    /**
     * 主键
     */
    private String id;

    /**
     * 属性ID
     */
    private String propertyId;

    /**
     * 属性编码
     */
    private String propertyCode;

    /**
     * 维度ID
     */
    private String dimId;

    /**
     * 维度编码
     */
    private String dimCode;

    /**
     * 同步状态
     */
    private SyncStatus syncStatus;

    /**
     * 错误信息
     */
    private String errorMessage;
}

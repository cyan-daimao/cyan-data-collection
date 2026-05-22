package com.cyan.datacollection.application.collect.bo;

import com.cyan.datacollection.enums.ValidateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 属性校验结果业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ValidateResultBO {

    /**
     * 校验状态
     */
    private ValidateStatus status;

    /**
     * 错误明细列表
     */
    private List<String> errors;
}

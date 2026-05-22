package com.cyan.datacollection.application.collect.bo;

import com.cyan.datacollection.enums.ValidateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 事件上报结果业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CollectResultBO {

    private Boolean accepted;
    private String sampleId;
    private ValidateStatus validateStatus;
    private List<String> errors;
}

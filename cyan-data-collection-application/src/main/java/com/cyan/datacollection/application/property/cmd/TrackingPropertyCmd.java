package com.cyan.datacollection.application.property.cmd;

import com.cyan.datacollection.enums.DataType;
import com.cyan.datacollection.enums.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 属性定义命令
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingPropertyCmd {

    private String propertyCode;
    private String propertyName;
    private PropertyType propertyType;
    private DataType dataType;
    private String description;
    private Boolean isRequired;
    private Boolean isSensitive;
    private String securityLevel;
    private List<String> enumValues;
    private Integer maxLength;
    private String validationRule;
    private String standardCode;
    private String createBy;
    private String updateBy;
}

package com.cyan.datacollection.application.collect.impl;

import com.cyan.datacollection.application.collect.TrackingEventValidateService;
import com.cyan.datacollection.application.collect.bo.ValidateResultBO;
import com.cyan.datacollection.domain.event.TrackingEvent;
import com.cyan.datacollection.domain.eventproperty.EventPropertyRule;
import com.cyan.datacollection.domain.eventproperty.repository.TrackingEventPropertyRepository;
import com.cyan.datacollection.enums.DataType;
import com.cyan.datacollection.enums.ValidateStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 事件属性校验服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingEventValidateServiceImpl implements TrackingEventValidateService {

    private final TrackingEventPropertyRepository trackingEventPropertyRepository;

    public TrackingEventValidateServiceImpl(TrackingEventPropertyRepository trackingEventPropertyRepository) {
        this.trackingEventPropertyRepository = trackingEventPropertyRepository;
    }

    @Override
    public ValidateResultBO validate(TrackingEvent event, Map<String, Object> properties) {
        if (event == null) {
            return new ValidateResultBO()
                    .setStatus(ValidateStatus.FAIL)
                    .setErrors(List.of("[FAIL] 事件不存在"));
        }

        List<EventPropertyRule> rules = trackingEventPropertyRepository.findPropertyRulesByEventId(event.getId());
        Map<String, EventPropertyRule> ruleMap = new HashMap<>();
        for (EventPropertyRule rule : rules) {
            ruleMap.put(rule.getPropertyCode(), rule);
        }

        List<String> failErrors = new ArrayList<>();
        List<String> warnErrors = new ArrayList<>();

        // 1. 校验规则内的属性（必填、类型、枚举、长度、正则）
        for (EventPropertyRule rule : rules) {
            String code = rule.getPropertyCode();
            Object value = properties != null ? properties.get(code) : null;

            // 必填校验
            if (Boolean.TRUE.equals(rule.getIsRequired()) && isEmptyValue(value)) {
                failErrors.add("[FAIL] 缺少必填属性 " + code);
                continue;
            }

            // 值为空且非必填，跳过后续校验
            if (isEmptyValue(value)) {
                continue;
            }

            // 类型校验
            String typeError = validateType(code, value, rule.getDataType());
            if (typeError != null) {
                failErrors.add(typeError);
                continue;
            }

            // 枚举校验
            if (rule.getEnumValues() != null && !rule.getEnumValues().isEmpty()) {
                String enumError = validateEnum(code, value, rule.getEnumValues());
                if (enumError != null) {
                    failErrors.add(enumError);
                }
            }

            // 最大长度校验（仅字符串）
            if (rule.getMaxLength() != null && rule.getMaxLength() > 0
                    && rule.getDataType() == DataType.STRING && value instanceof String str) {
                if (str.length() > rule.getMaxLength()) {
                    failErrors.add("[FAIL] 属性 " + code + " 长度超过限制，最大 " + rule.getMaxLength());
                }
            }

            // 正则校验
            if (rule.getValidationRule() != null && !rule.getValidationRule().isEmpty()
                    && value instanceof String str) {
                if (!Pattern.matches(rule.getValidationRule(), str)) {
                    failErrors.add("[FAIL] 属性 " + code + " 格式不符合规则 " + rule.getValidationRule());
                }
            }
        }

        // 2. 校验未知属性
        if (properties != null) {
            for (String key : properties.keySet()) {
                if (!ruleMap.containsKey(key)) {
                    warnErrors.add("[WARN] 未定义属性 " + key);
                }
            }
        }

        // 3. 确定状态
        List<String> allErrors = new ArrayList<>();
        allErrors.addAll(failErrors);
        allErrors.addAll(warnErrors);

        ValidateStatus status;
        if (!failErrors.isEmpty()) {
            status = ValidateStatus.FAIL;
        } else if (!warnErrors.isEmpty()) {
            status = ValidateStatus.WARN;
        } else {
            status = ValidateStatus.PASS;
        }

        return new ValidateResultBO().setStatus(status).setErrors(allErrors);
    }

    private boolean isEmptyValue(Object value) {
        return value == null || (value instanceof String str && str.isEmpty());
    }

    private String validateType(String code, Object value, DataType dataType) {
        if (dataType == null) {
            return null;
        }
        if (dataType == DataType.STRING) {
            return value instanceof String ? null : "[FAIL] 属性 " + code + " 类型错误，期望 STRING";
        }
        if (dataType == DataType.NUMBER) {
            return value instanceof Number ? null : "[FAIL] 属性 " + code + " 类型错误，期望 NUMBER";
        }
        if (dataType == DataType.BOOLEAN) {
            return value instanceof Boolean ? null : "[FAIL] 属性 " + code + " 类型错误，期望 BOOLEAN";
        }
        if (dataType == DataType.DATE) {
            return validateDate(code, value);
        }
        if (dataType == DataType.DATETIME) {
            return validateDateTime(code, value);
        }
        if (dataType == DataType.ENUM) {
            return value instanceof String || value instanceof Number ? null : "[FAIL] 属性 " + code + " 类型错误，期望 ENUM";
        }
        if (dataType == DataType.ARRAY) {
            return (value instanceof Collection || value.getClass().isArray()) ? null : "[FAIL] 属性 " + code + " 类型错误，期望 ARRAY";
        }
        if (dataType == DataType.OBJECT) {
            return value instanceof Map ? null : "[FAIL] 属性 " + code + " 类型错误，期望 OBJECT";
        }
        return null;
    }

    private String validateDate(String code, Object value) {
        if (!(value instanceof String str)) {
            return "[FAIL] 属性 " + code + " 类型错误，期望 DATE(yyyy-MM-dd)";
        }
        try {
            LocalDate.parse(str, DateTimeFormatter.ISO_LOCAL_DATE);
            return null;
        } catch (DateTimeParseException e) {
            return "[FAIL] 属性 " + code + " 日期格式错误，期望 yyyy-MM-dd";
        }
    }

    private String validateDateTime(String code, Object value) {
        if (!(value instanceof String str)) {
            return "[FAIL] 属性 " + code + " 类型错误，期望 DATETIME";
        }
        try {
            // 支持 ISO 格式（含时区）和常见格式
            if (str.contains("T")) {
                if (str.contains("+") || str.endsWith("Z")) {
                    java.time.OffsetDateTime.parse(str, DateTimeFormatter.ISO_DATE_TIME);
                } else {
                    LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }
            } else {
                LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            return null;
        } catch (DateTimeParseException e) {
            return "[FAIL] 属性 " + code + " 日期时间格式错误";
        }
    }

    private String validateEnum(String code, Object value, List<String> enumValues) {
        String strValue = String.valueOf(value);
        if (!enumValues.contains(strValue)) {
            return "[FAIL] 属性 " + code + " 枚举值非法，期望 " + enumValues;
        }
        return null;
    }
}

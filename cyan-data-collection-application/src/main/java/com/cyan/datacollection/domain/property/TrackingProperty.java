package com.cyan.datacollection.domain.property;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.property.repository.TrackingPropertyRepository;
import com.cyan.datacollection.enums.DataType;
import com.cyan.datacollection.enums.PropertyStatus;
import com.cyan.datacollection.enums.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 属性定义
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingProperty {

    /**
     * 主键
     */
    private String id;

    /**
     * 属性编码
     */
    private String propertyCode;

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 属性类型
     */
    private PropertyType propertyType;

    /**
     * 数据类型
     */
    private DataType dataType;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否默认必填
     */
    private Boolean isRequired;

    /**
     * 是否敏感
     */
    private Boolean isSensitive;

    /**
     * 安全等级
     */
    private String securityLevel;

    /**
     * 枚举值
     */
    private List<String> enumValues;

    /**
     * 最大长度
     */
    private Integer maxLength;

    /**
     * 校验规则
     */
    private String validationRule;

    /**
     * 关联数据标准编码
     */
    private String standardCode;

    /**
     * 状态
     */
    private PropertyStatus status;

    /**
     * 版本号
     */
    private Integer version;

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
     * 逻辑删除时间
     */
    private LocalDateTime deletedAt;

    private void validate() {
        Assert.notBlank(this.propertyCode, new SilentException("属性编码不能为空"));
        Assert.notBlank(this.propertyName, new SilentException("属性名称不能为空"));
        Assert.notNull(this.propertyType, new SilentException("属性类型不能为空"));
        Assert.notNull(this.dataType, new SilentException("数据类型不能为空"));
        if (this.dataType == DataType.ENUM) {
            Assert.notEmpty(this.enumValues, new SilentException("枚举类型必须配置枚举值"));
        }
        if (Boolean.TRUE.equals(this.isSensitive)) {
            Assert.notBlank(this.securityLevel, new SilentException("敏感属性必须填写安全等级"));
        }
    }

    public TrackingProperty save(TrackingPropertyRepository repository) {
        validate();
        this.status = PropertyStatus.DRAFT;
        this.version = 1;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    public TrackingProperty update(TrackingPropertyRepository repository) {
        validate();
        Assert.notBlank(this.id, new SilentException("属性ID不能为空"));
        Assert.isTrue(this.status == PropertyStatus.DRAFT, new SilentException("只有草稿状态可编辑"));
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    public TrackingProperty publish(TrackingPropertyRepository repository) {
        Assert.notNull(this.status, new SilentException("属性状态异常"));
        if (this.status == PropertyStatus.PUBLISHED) {
            throw new SilentException("属性已发布");
        }
        if (this.status == PropertyStatus.DEPRECATED) {
            throw new SilentException("已废弃属性不可发布");
        }
        this.status = PropertyStatus.PUBLISHED;
        this.version = (this.version == null ? 1 : this.version) + 1;
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    public TrackingProperty deprecate(TrackingPropertyRepository repository) {
        Assert.notNull(this.status, new SilentException("属性状态异常"));
        if (this.status == PropertyStatus.DEPRECATED) {
            throw new SilentException("属性已废弃");
        }
        this.status = PropertyStatus.DEPRECATED;
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    public void delete(TrackingPropertyRepository repository) {
        Assert.notBlank(this.id, new SilentException("属性ID不能为空"));
        repository.deleteById(this.id);
    }
}

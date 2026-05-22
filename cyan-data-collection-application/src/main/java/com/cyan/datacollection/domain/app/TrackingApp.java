package com.cyan.datacollection.domain.app;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.domain.app.repository.TrackingAppRepository;
import com.cyan.datacollection.enums.AppStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * 接入应用
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TrackingApp {

    /**
     * 主键
     */
    private String id;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用类型
     */
    private String appType;

    /**
     * 描述
     */
    private String description;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 上报地址
     */
    private String reportUrl;

    /**
     * 状态
     */
    private AppStatus status;

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

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private void validate() {
        Assert.notBlank(this.appCode, new SilentException("应用编码不能为空"));
        Assert.notBlank(this.appName, new SilentException("应用名称不能为空"));
    }

    private static String generateSecretKey() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public TrackingApp save(TrackingAppRepository repository) {
        validate();
        this.status = AppStatus.ENABLED;
        if (this.secretKey == null || this.secretKey.isBlank()) {
            this.secretKey = generateSecretKey();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        return repository.save(this);
    }

    public TrackingApp update(TrackingAppRepository repository) {
        validate();
        Assert.notBlank(this.id, new SilentException("应用ID不能为空"));
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    public TrackingApp rotateSecret(TrackingAppRepository repository) {
        Assert.notBlank(this.id, new SilentException("应用ID不能为空"));
        this.secretKey = generateSecretKey();
        this.updatedAt = LocalDateTime.now();
        return repository.update(this);
    }

    public void delete(TrackingAppRepository repository) {
        Assert.notBlank(this.id, new SilentException("应用ID不能为空"));
        repository.deleteById(this.id);
    }
}

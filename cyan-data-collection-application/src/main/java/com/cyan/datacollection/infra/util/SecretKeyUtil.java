package com.cyan.datacollection.infra.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密钥工具类
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Component
public class SecretKeyUtil {

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * 生成随机密钥
     */
    public String generateSecretKey() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}

package com.cyan.datacollection.adapter.release.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 创建发布版本请求
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingReleaseCreateRequest {

    /**
     * 方案ID
     */
    @NotBlank(message = "方案ID不能为空")
    private String planId;
}

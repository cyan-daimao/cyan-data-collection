package com.cyan.datacollection.infra.persistence.release.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 埋点发布版本明细表
 *
 * @author cy.Y
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("tracking_release_item")
public class TrackingReleaseItemDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发布ID
     */
    @TableField("release_id")
    private Long releaseId;

    /**
     * 对象类型
     */
    @TableField("item_type")
    private String itemType;

    /**
     * 对象ID
     */
    @TableField("item_id")
    private Long itemId;

    /**
     * 对象编码
     */
    @TableField("item_code")
    private String itemCode;

    /**
     * 变更类型
     */
    @TableField("change_type")
    private String changeType;

    /**
     * 发布快照JSON
     */
    @TableField("snapshot")
    private String snapshot;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除
     */
    @TableField("deleted_at")
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;
}

package com.cyan.datacollection.infra.persistence.qualityrule.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyan.datacollection.infra.persistence.qualityrule.dos.TrackingQualityRuleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 质量规则配置 Mapper
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Mapper
public interface TrackingQualityRuleMapper extends BaseMapper<TrackingQualityRuleDO> {

    /**
     * 查询所有启用的规则
     */
    @Select("SELECT * FROM tracking_quality_rule WHERE deleted_at IS NULL AND is_enabled = 1")
    List<TrackingQualityRuleDO> selectEnabledRules();
}

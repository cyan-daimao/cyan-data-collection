package com.cyan.datacollection.infra.persistence.qualityrule.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyan.datacollection.domain.qualityrule.TrackingQualityRule;
import com.cyan.datacollection.domain.qualityrule.query.TrackingQualityRulePageQuery;
import com.cyan.datacollection.domain.qualityrule.repository.TrackingQualityRuleRepository;
import com.cyan.datacollection.infra.persistence.qualityrule.convert.TrackingQualityRuleInfraConvert;
import com.cyan.datacollection.infra.persistence.qualityrule.dos.TrackingQualityRuleDO;
import com.cyan.datacollection.infra.persistence.qualityrule.mappers.TrackingQualityRuleMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 质量规则配置仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingQualityRuleRepositoryImpl implements TrackingQualityRuleRepository {

    private final TrackingQualityRuleMapper trackingQualityRuleMapper;
    private final TrackingQualityRuleInfraConvert trackingQualityRuleInfraConvert;

    public TrackingQualityRuleRepositoryImpl(TrackingQualityRuleMapper trackingQualityRuleMapper,
                                             TrackingQualityRuleInfraConvert trackingQualityRuleInfraConvert) {
        this.trackingQualityRuleMapper = trackingQualityRuleMapper;
        this.trackingQualityRuleInfraConvert = trackingQualityRuleInfraConvert;
    }

    @Override
    public TrackingQualityRule findById(String id) {
        TrackingQualityRuleDO dos = trackingQualityRuleMapper.selectById(id);
        return dos == null ? null : trackingQualityRuleInfraConvert.toDomain(dos);
    }

    @Override
    public com.cyan.arch.common.api.Page<TrackingQualityRule> page(TrackingQualityRulePageQuery query) {
        Page<TrackingQualityRuleDO> page = new Page<>(query.current(), query.size());
        LambdaQueryWrapper<TrackingQualityRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrackingQualityRuleDO::getDeletedAt, null);
        if (StringUtils.isNotBlank(query.getRuleCode())) {
            wrapper.like(TrackingQualityRuleDO::getRuleCode, query.getRuleCode());
        }
        if (StringUtils.isNotBlank(query.getRuleName())) {
            wrapper.like(TrackingQualityRuleDO::getRuleName, query.getRuleName());
        }
        if (StringUtils.isNotBlank(query.getEventCode())) {
            wrapper.eq(TrackingQualityRuleDO::getEventCode, query.getEventCode());
        }
        if (StringUtils.isNotBlank(query.getAppCode())) {
            wrapper.eq(TrackingQualityRuleDO::getAppCode, query.getAppCode());
        }
        if (StringUtils.isNotBlank(query.getAlertType())) {
            wrapper.eq(TrackingQualityRuleDO::getAlertType, query.getAlertType());
        }
        if (StringUtils.isNotBlank(query.getAlertLevel())) {
            wrapper.eq(TrackingQualityRuleDO::getAlertLevel, query.getAlertLevel());
        }
        if (query.getIsEnabled() != null) {
            wrapper.eq(TrackingQualityRuleDO::getIsEnabled, query.getIsEnabled() ? 1 : 0);
        }
        wrapper.orderByDesc(TrackingQualityRuleDO::getCreatedAt);

        Page<TrackingQualityRuleDO> result = trackingQualityRuleMapper.selectPage(page, wrapper);
        List<TrackingQualityRule> list = Optional.ofNullable(result.getRecords()).orElse(List.of()).stream()
                .map(trackingQualityRuleInfraConvert::toDomain)
                .toList();
        return new com.cyan.arch.common.api.Page<>(list, result.getCurrent(), result.getSize(), result.getTotal());
    }

    @Override
    public List<TrackingQualityRule> findEnabledRules() {
        List<TrackingQualityRuleDO> dos = trackingQualityRuleMapper.selectEnabledRules();
        return trackingQualityRuleInfraConvert.toDomainList(dos);
    }

    @Override
    public TrackingQualityRule save(TrackingQualityRule rule) {
        TrackingQualityRuleDO dos = trackingQualityRuleInfraConvert.toDO(rule);
        trackingQualityRuleMapper.insert(dos);
        return trackingQualityRuleInfraConvert.toDomain(dos);
    }

    @Override
    public TrackingQualityRule update(TrackingQualityRule rule) {
        TrackingQualityRuleDO dos = trackingQualityRuleInfraConvert.toDO(rule);
        trackingQualityRuleMapper.updateById(dos);
        return trackingQualityRuleInfraConvert.toDomain(dos);
    }

    @Override
    public void deleteById(String id) {
        trackingQualityRuleMapper.deleteById(id);
    }
}

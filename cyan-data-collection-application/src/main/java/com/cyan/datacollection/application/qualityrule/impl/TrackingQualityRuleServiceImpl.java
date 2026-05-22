package com.cyan.datacollection.application.qualityrule.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.qualityrule.TrackingQualityRuleService;
import com.cyan.datacollection.application.qualityrule.bo.TrackingQualityRuleBO;
import com.cyan.datacollection.application.qualityrule.cmd.TrackingQualityRuleCmd;
import com.cyan.datacollection.application.qualityrule.convert.TrackingQualityRuleAppConvert;
import com.cyan.datacollection.domain.qualityrule.query.TrackingQualityRulePageQuery;
import com.cyan.datacollection.domain.qualityrule.TrackingQualityRule;
import com.cyan.datacollection.domain.qualityrule.repository.TrackingQualityRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 质量规则配置服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Service
public class TrackingQualityRuleServiceImpl implements TrackingQualityRuleService {

    private final TrackingQualityRuleRepository trackingQualityRuleRepository;

    public TrackingQualityRuleServiceImpl(TrackingQualityRuleRepository trackingQualityRuleRepository) {
        this.trackingQualityRuleRepository = trackingQualityRuleRepository;
    }

    @Override
    @Transactional
    public TrackingQualityRuleBO create(TrackingQualityRuleCmd cmd) {
        TrackingQualityRule rule = TrackingQualityRuleAppConvert.INSTANCE.toDomain(cmd);
        rule.setIsEnabled(true);
        rule = trackingQualityRuleRepository.save(rule);
        return TrackingQualityRuleAppConvert.INSTANCE.toBO(rule);
    }

    @Override
    @Transactional
    public TrackingQualityRuleBO update(String id, TrackingQualityRuleCmd cmd) {
        TrackingQualityRule rule = trackingQualityRuleRepository.findById(id);
        Assert.notNull(rule, new SilentException("规则不存在"));
        TrackingQualityRule updated = TrackingQualityRuleAppConvert.INSTANCE.toDomain(cmd);
        updated.setId(id);
        updated.setIsEnabled(rule.getIsEnabled());
        updated = trackingQualityRuleRepository.update(updated);
        return TrackingQualityRuleAppConvert.INSTANCE.toBO(updated);
    }

    @Override
    public TrackingQualityRuleBO getById(String id) {
        TrackingQualityRule rule = trackingQualityRuleRepository.findById(id);
        Assert.notNull(rule, new SilentException("规则不存在"));
        return TrackingQualityRuleAppConvert.INSTANCE.toBO(rule);
    }

    @Override
    public Page<TrackingQualityRuleBO> page(TrackingQualityRulePageQuery query) {
        com.cyan.arch.common.api.Page<TrackingQualityRule> domainPage = trackingQualityRuleRepository.page(query);
        List<TrackingQualityRuleBO> bos = domainPage.getData().stream()
                .map(TrackingQualityRuleAppConvert.INSTANCE::toBO)
                .toList();
        return new Page<>(bos, domainPage.getCurrent(), domainPage.getSize(), domainPage.getTotal());
    }

    @Override
    @Transactional
    public TrackingQualityRuleBO enable(String id) {
        TrackingQualityRule rule = trackingQualityRuleRepository.findById(id);
        Assert.notNull(rule, new SilentException("规则不存在"));
        rule.enable(trackingQualityRuleRepository);
        return TrackingQualityRuleAppConvert.INSTANCE.toBO(rule);
    }

    @Override
    @Transactional
    public TrackingQualityRuleBO disable(String id) {
        TrackingQualityRule rule = trackingQualityRuleRepository.findById(id);
        Assert.notNull(rule, new SilentException("规则不存在"));
        rule.disable(trackingQualityRuleRepository);
        return TrackingQualityRuleAppConvert.INSTANCE.toBO(rule);
    }

    @Override
    public List<TrackingQualityRuleBO> listEnabledRules() {
        return trackingQualityRuleRepository.findEnabledRules().stream()
                .map(TrackingQualityRuleAppConvert.INSTANCE::toBO)
                .toList();
    }
}

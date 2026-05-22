package com.cyan.datacollection.application.property.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.property.TrackingPropertyService;
import com.cyan.datacollection.application.property.bo.TrackingPropertyBO;
import com.cyan.datacollection.application.property.cmd.TrackingPropertyCmd;
import com.cyan.datacollection.application.property.convert.TrackingPropertyAppConvert;
import com.cyan.datacollection.domain.property.TrackingProperty;
import com.cyan.datacollection.domain.property.query.TrackingPropertyPageQuery;
import com.cyan.datacollection.domain.property.repository.TrackingPropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 属性定义服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingPropertyServiceImpl implements TrackingPropertyService {

    private final TrackingPropertyRepository trackingPropertyRepository;

    public TrackingPropertyServiceImpl(TrackingPropertyRepository trackingPropertyRepository) {
        this.trackingPropertyRepository = trackingPropertyRepository;
    }

    @Override
    public Page<TrackingPropertyBO> page(TrackingPropertyPageQuery query) {
        Page<TrackingProperty> page = trackingPropertyRepository.page(query);
        List<TrackingPropertyBO> list = page.getData().stream()
                .map(TrackingPropertyAppConvert.INSTANCE::toTrackingPropertyBO)
                .toList();
        return new Page<>(list, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    @Transactional
    public TrackingPropertyBO create(TrackingPropertyCmd cmd) {
        TrackingProperty existing = trackingPropertyRepository.findByCode(cmd.getPropertyCode());
        Assert.isNull(existing, new SilentException("属性编码已存在"));

        TrackingProperty property = TrackingPropertyAppConvert.INSTANCE.toTrackingProperty(cmd);
        property = property.save(trackingPropertyRepository);
        return TrackingPropertyAppConvert.INSTANCE.toTrackingPropertyBO(property);
    }

    @Override
    @Transactional
    public TrackingPropertyBO update(String id, TrackingPropertyCmd cmd) {
        TrackingProperty existing = trackingPropertyRepository.findById(id);
        Assert.notNull(existing, new SilentException("属性不存在"));
        Assert.isTrue(existing.getStatus() == com.cyan.datacollection.enums.PropertyStatus.DRAFT,
                new SilentException("只有草稿状态可编辑"));

        TrackingProperty property = TrackingPropertyAppConvert.INSTANCE.toTrackingProperty(cmd);
        property.setId(existing.getId());
        property.setPropertyCode(existing.getPropertyCode());
        property.setCreateBy(existing.getCreateBy());
        property.setStatus(existing.getStatus());
        property.setVersion(existing.getVersion());
        property = property.update(trackingPropertyRepository);
        return TrackingPropertyAppConvert.INSTANCE.toTrackingPropertyBO(property);
    }

    @Override
    public TrackingPropertyBO detail(String id) {
        TrackingProperty property = trackingPropertyRepository.findById(id);
        Assert.notNull(property, new SilentException("属性不存在"));
        return TrackingPropertyAppConvert.INSTANCE.toTrackingPropertyBO(property);
    }

    @Override
    @Transactional
    public TrackingPropertyBO publish(String id) {
        TrackingProperty property = trackingPropertyRepository.findById(id);
        Assert.notNull(property, new SilentException("属性不存在"));
        property = property.publish(trackingPropertyRepository);
        return TrackingPropertyAppConvert.INSTANCE.toTrackingPropertyBO(property);
    }

    @Override
    @Transactional
    public TrackingPropertyBO deprecate(String id) {
        TrackingProperty property = trackingPropertyRepository.findById(id);
        Assert.notNull(property, new SilentException("属性不存在"));
        property = property.deprecate(trackingPropertyRepository);
        return TrackingPropertyAppConvert.INSTANCE.toTrackingPropertyBO(property);
    }

    @Override
    public TrackingPropertyBO.UsageBO usage(String id) {
        TrackingProperty property = trackingPropertyRepository.findById(id);
        Assert.notNull(property, new SilentException("属性不存在"));
        // MVP 简化：返回基础统计
        TrackingPropertyBO.UsageBO usage = new TrackingPropertyBO.UsageBO();
        usage.setPropertyId(id);
        usage.setEventCount(0);
        return usage;
    }

    @Override
    public List<TrackingPropertyBO> findByIds(List<String> ids) {
        List<TrackingProperty> list = trackingPropertyRepository.findByIds(ids);
        return list.stream()
                .map(TrackingPropertyAppConvert.INSTANCE::toTrackingPropertyBO)
                .toList();
    }
}

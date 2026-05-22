package com.cyan.datacollection.infra.persistence.property.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyan.arch.common.api.Pageable;
import com.cyan.datacollection.domain.property.TrackingProperty;
import com.cyan.datacollection.domain.property.query.TrackingPropertyPageQuery;
import com.cyan.datacollection.domain.property.repository.TrackingPropertyRepository;
import com.cyan.datacollection.enums.DataType;
import com.cyan.datacollection.enums.PropertyStatus;
import com.cyan.datacollection.enums.PropertyType;
import com.cyan.datacollection.infra.persistence.property.convert.TrackingPropertyInfraConvert;
import com.cyan.datacollection.infra.persistence.property.dos.TrackingPropertyDO;
import com.cyan.datacollection.infra.persistence.property.mappers.TrackingPropertyMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 属性定义仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingPropertyRepositoryImpl implements TrackingPropertyRepository {

    private final TrackingPropertyMapper trackingPropertyMapper;

    public TrackingPropertyRepositoryImpl(TrackingPropertyMapper trackingPropertyMapper) {
        this.trackingPropertyMapper = trackingPropertyMapper;
    }

    @Override
    public TrackingProperty findById(String id) {
        TrackingPropertyDO trackingPropertyDO = trackingPropertyMapper.selectById(Long.parseLong(id));
        return TrackingPropertyInfraConvert.INSTANCE.toTrackingProperty(trackingPropertyDO);
    }

    @Override
    public com.cyan.arch.common.api.Page<TrackingProperty> page(TrackingPropertyPageQuery query) {
        Page<TrackingPropertyDO> page = new Page<>(query.current(), query.size());
        LambdaQueryWrapper<TrackingPropertyDO> wrapper = new LambdaQueryWrapper<TrackingPropertyDO>()
                .like(StringUtils.isNotBlank(query.getPropertyCode()), TrackingPropertyDO::getPropertyCode, query.getPropertyCode())
                .like(StringUtils.isNotBlank(query.getPropertyName()), TrackingPropertyDO::getPropertyName, query.getPropertyName())
                .eq(StringUtils.isNotBlank(query.getPropertyType()), TrackingPropertyDO::getPropertyType, PropertyType.of(query.getPropertyType()))
                .eq(StringUtils.isNotBlank(query.getDataType()), TrackingPropertyDO::getDataType, DataType.of(query.getDataType()))
                .eq(StringUtils.isNotBlank(query.getStatus()), TrackingPropertyDO::getStatus, PropertyStatus.of(query.getStatus()))
                .orderByDesc(TrackingPropertyDO::getUpdatedAt);
        Page<TrackingPropertyDO> result = trackingPropertyMapper.selectPage(page, wrapper);
        List<TrackingProperty> list = Optional.ofNullable(result.getRecords()).orElse(List.of()).stream()
                .map(TrackingPropertyInfraConvert.INSTANCE::toTrackingProperty)
                .toList();
        return new com.cyan.arch.common.api.Page<>(list, result.getCurrent(), result.getSize(), result.getTotal());
    }

    @Override
    public TrackingProperty findByCode(String propertyCode) {
        LambdaQueryWrapper<TrackingPropertyDO> wrapper = new LambdaQueryWrapper<TrackingPropertyDO>()
                .eq(TrackingPropertyDO::getPropertyCode, propertyCode);
        TrackingPropertyDO trackingPropertyDO = trackingPropertyMapper.selectOne(wrapper);
        return TrackingPropertyInfraConvert.INSTANCE.toTrackingProperty(trackingPropertyDO);
    }

    @Override
    public TrackingProperty save(TrackingProperty property) {
        TrackingPropertyDO trackingPropertyDO = TrackingPropertyInfraConvert.INSTANCE.toTrackingPropertyDO(property);
        trackingPropertyMapper.insert(trackingPropertyDO);
        return findById(String.valueOf(trackingPropertyDO.getId()));
    }

    @Override
    public TrackingProperty update(TrackingProperty property) {
        TrackingPropertyDO trackingPropertyDO = TrackingPropertyInfraConvert.INSTANCE.toTrackingPropertyDO(property);
        trackingPropertyDO.setId(Long.parseLong(property.getId()));
        trackingPropertyMapper.updateById(trackingPropertyDO);
        return findById(property.getId());
    }

    @Override
    public void deleteById(String id) {
        trackingPropertyMapper.deleteById(Long.parseLong(id));
    }

    @Override
    public List<TrackingProperty> findByIds(List<String> ids) {
        List<Long> idList = ids.stream().map(Long::parseLong).toList();
        List<TrackingPropertyDO> dos = trackingPropertyMapper.selectBatchIds(idList);
        return Optional.ofNullable(dos).orElse(List.of()).stream()
                .map(TrackingPropertyInfraConvert.INSTANCE::toTrackingProperty)
                .toList();
    }
}

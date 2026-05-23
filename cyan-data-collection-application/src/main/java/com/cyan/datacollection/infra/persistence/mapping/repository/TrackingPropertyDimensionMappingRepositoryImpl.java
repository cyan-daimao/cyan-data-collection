package com.cyan.datacollection.infra.persistence.mapping.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyan.datacollection.domain.mapping.TrackingPropertyDimensionMapping;
import com.cyan.datacollection.domain.mapping.repository.TrackingPropertyDimensionMappingRepository;
import com.cyan.datacollection.infra.persistence.mapping.convert.TrackingMappingInfraConvert;
import com.cyan.datacollection.infra.persistence.mapping.dos.TrackingPropertyDimensionMappingDO;
import com.cyan.datacollection.infra.persistence.mapping.mappers.TrackingPropertyDimensionMappingMapper;
import org.springframework.stereotype.Repository;

/**
 * 采集属性维度映射仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingPropertyDimensionMappingRepositoryImpl implements TrackingPropertyDimensionMappingRepository {

    private final TrackingPropertyDimensionMappingMapper mapper;

    public TrackingPropertyDimensionMappingRepositoryImpl(TrackingPropertyDimensionMappingMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public TrackingPropertyDimensionMapping findByPropertyId(String propertyId) {
        TrackingPropertyDimensionMappingDO data = mapper.selectOne(new LambdaQueryWrapper<TrackingPropertyDimensionMappingDO>()
                .eq(TrackingPropertyDimensionMappingDO::getPropertyId, Long.parseLong(propertyId)));
        return TrackingMappingInfraConvert.INSTANCE.toPropertyDimensionMapping(data);
    }

    @Override
    public TrackingPropertyDimensionMapping findByDimCode(String dimCode) {
        TrackingPropertyDimensionMappingDO data = mapper.selectOne(new LambdaQueryWrapper<TrackingPropertyDimensionMappingDO>()
                .eq(TrackingPropertyDimensionMappingDO::getDimCode, dimCode));
        return TrackingMappingInfraConvert.INSTANCE.toPropertyDimensionMapping(data);
    }

    @Override
    public TrackingPropertyDimensionMapping save(TrackingPropertyDimensionMapping mapping) {
        TrackingPropertyDimensionMappingDO data = TrackingMappingInfraConvert.INSTANCE.toPropertyDimensionMappingDO(mapping);
        mapper.insert(data);
        return TrackingMappingInfraConvert.INSTANCE.toPropertyDimensionMapping(mapper.selectById(data.getId()));
    }

    @Override
    public TrackingPropertyDimensionMapping update(TrackingPropertyDimensionMapping mapping) {
        TrackingPropertyDimensionMappingDO data = TrackingMappingInfraConvert.INSTANCE.toPropertyDimensionMappingDO(mapping);
        mapper.updateById(data);
        return TrackingMappingInfraConvert.INSTANCE.toPropertyDimensionMapping(mapper.selectById(data.getId()));
    }
}

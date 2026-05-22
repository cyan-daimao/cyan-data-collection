package com.cyan.datacollection.infra.persistence.release.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyan.datacollection.domain.release.TrackingReleaseItem;
import com.cyan.datacollection.domain.release.repository.TrackingReleaseItemRepository;
import com.cyan.datacollection.infra.persistence.release.convert.TrackingReleaseInfraConvert;
import com.cyan.datacollection.infra.persistence.release.dos.TrackingReleaseItemDO;
import com.cyan.datacollection.infra.persistence.release.mappers.TrackingReleaseItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 埋点发布版本明细仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingReleaseItemRepositoryImpl implements TrackingReleaseItemRepository {

    private final TrackingReleaseItemMapper trackingReleaseItemMapper;

    public TrackingReleaseItemRepositoryImpl(TrackingReleaseItemMapper trackingReleaseItemMapper) {
        this.trackingReleaseItemMapper = trackingReleaseItemMapper;
    }

    @Override
    public List<TrackingReleaseItem> findByReleaseId(String releaseId) {
        LambdaQueryWrapper<TrackingReleaseItemDO> wrapper = new LambdaQueryWrapper<TrackingReleaseItemDO>()
                .eq(TrackingReleaseItemDO::getReleaseId, Long.parseLong(releaseId))
                .orderByAsc(TrackingReleaseItemDO::getItemType)
                .orderByAsc(TrackingReleaseItemDO::getId);
        List<TrackingReleaseItemDO> dos = trackingReleaseItemMapper.selectList(wrapper);
        return Optional.ofNullable(dos).orElse(List.of()).stream()
                .map(TrackingReleaseInfraConvert.INSTANCE::toDomain)
                .toList();
    }

    @Override
    public TrackingReleaseItem save(TrackingReleaseItem item) {
        TrackingReleaseItemDO dos = TrackingReleaseInfraConvert.INSTANCE.toDO(item);
        trackingReleaseItemMapper.insert(dos);
        return findById(String.valueOf(dos.getId()));
    }

    @Override
    public void saveBatch(List<TrackingReleaseItem> items) {
        List<TrackingReleaseItemDO> dosList = items.stream()
                .map(TrackingReleaseInfraConvert.INSTANCE::toDO)
                .toList();
        for (TrackingReleaseItemDO dos : dosList) {
            trackingReleaseItemMapper.insert(dos);
        }
    }

    @Override
    public void deleteById(String id) {
        trackingReleaseItemMapper.deleteById(Long.parseLong(id));
    }

    @Override
    public void deleteByReleaseId(String releaseId) {
        LambdaQueryWrapper<TrackingReleaseItemDO> wrapper = new LambdaQueryWrapper<TrackingReleaseItemDO>()
                .eq(TrackingReleaseItemDO::getReleaseId, Long.parseLong(releaseId));
        trackingReleaseItemMapper.delete(wrapper);
    }

    private TrackingReleaseItem findById(String id) {
        TrackingReleaseItemDO dos = trackingReleaseItemMapper.selectById(Long.parseLong(id));
        return TrackingReleaseInfraConvert.INSTANCE.toDomain(dos);
    }
}

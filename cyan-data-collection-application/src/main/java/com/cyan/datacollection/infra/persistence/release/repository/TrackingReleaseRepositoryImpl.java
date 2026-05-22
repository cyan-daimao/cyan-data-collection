package com.cyan.datacollection.infra.persistence.release.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyan.arch.common.api.Pageable;
import com.cyan.arch.common.util.StrUtils;
import com.cyan.datacollection.domain.release.TrackingRelease;
import com.cyan.datacollection.domain.release.query.TrackingReleasePageQuery;
import com.cyan.datacollection.domain.release.repository.TrackingReleaseRepository;
import com.cyan.datacollection.enums.ReleaseStatus;
import com.cyan.datacollection.infra.persistence.release.convert.TrackingReleaseInfraConvert;
import com.cyan.datacollection.infra.persistence.release.dos.TrackingReleaseDO;
import com.cyan.datacollection.infra.persistence.release.mappers.TrackingReleaseMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 埋点发布版本仓储实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Repository
public class TrackingReleaseRepositoryImpl implements TrackingReleaseRepository {

    private final TrackingReleaseMapper trackingReleaseMapper;

    public TrackingReleaseRepositoryImpl(TrackingReleaseMapper trackingReleaseMapper) {
        this.trackingReleaseMapper = trackingReleaseMapper;
    }

    @Override
    public TrackingRelease findById(String id) {
        TrackingReleaseDO dos = trackingReleaseMapper.selectById(Long.parseLong(id));
        return TrackingReleaseInfraConvert.INSTANCE.toDomain(dos);
    }

    @Override
    public com.cyan.arch.common.api.Page<TrackingRelease> page(TrackingReleasePageQuery query) {
        Page<TrackingReleaseDO> page = new Page<>(query.current(), query.size());
        LambdaQueryWrapper<TrackingReleaseDO> wrapper = new LambdaQueryWrapper<TrackingReleaseDO>()
                .eq(StrUtils.isNotBlank(query.getPlanId()), TrackingReleaseDO::getPlanId, query.getPlanId() != null ? Long.parseLong(query.getPlanId()) : null)
                .like(StringUtils.isNotBlank(query.getReleaseCode()), TrackingReleaseDO::getReleaseCode, query.getReleaseCode())
                .eq(StringUtils.isNotBlank(query.getStatus()), TrackingReleaseDO::getStatus, ReleaseStatus.of(query.getStatus()))
                .orderByDesc(TrackingReleaseDO::getCreatedAt);
        Page<TrackingReleaseDO> result = trackingReleaseMapper.selectPage(page, wrapper);
        List<TrackingRelease> list = Optional.ofNullable(result.getRecords()).orElse(List.of()).stream()
                .map(TrackingReleaseInfraConvert.INSTANCE::toDomain)
                .toList();
        return new com.cyan.arch.common.api.Page<>(list, result.getCurrent(), result.getSize(), result.getTotal());
    }

    @Override
    public TrackingRelease save(TrackingRelease release) {
        TrackingReleaseDO dos = TrackingReleaseInfraConvert.INSTANCE.toDO(release);
        trackingReleaseMapper.insert(dos);
        return findById(String.valueOf(dos.getId()));
    }

    @Override
    public TrackingRelease update(TrackingRelease release) {
        TrackingReleaseDO dos = TrackingReleaseInfraConvert.INSTANCE.toDO(release);
        dos.setId(Long.parseLong(release.getId()));
        trackingReleaseMapper.updateById(dos);
        return findById(release.getId());
    }

    @Override
    public void deleteById(String id) {
        trackingReleaseMapper.deleteById(Long.parseLong(id));
    }

    @Override
    public int findMaxSeqToday() {
        return trackingReleaseMapper.findMaxSeqToday();
    }
}

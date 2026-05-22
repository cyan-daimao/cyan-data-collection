package com.cyan.datacollection.application.demand.impl;

import com.cyan.arch.common.api.Assert;
import com.cyan.arch.common.api.Page;
import com.cyan.arch.common.api.SilentException;
import com.cyan.datacollection.application.demand.TrackingDemandService;
import com.cyan.datacollection.application.demand.bo.TrackingDemandBO;
import com.cyan.datacollection.application.demand.cmd.TrackingDemandCmd;
import com.cyan.datacollection.application.demand.convert.TrackingDemandAppConvert;
import com.cyan.datacollection.domain.demand.TrackingDemand;
import com.cyan.datacollection.domain.demand.query.TrackingDemandPageQuery;
import com.cyan.datacollection.domain.demand.repository.TrackingDemandRepository;
import com.cyan.datacollection.application.util.DemandCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 埋点需求服务实现
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Slf4j
@Service
public class TrackingDemandServiceImpl implements TrackingDemandService {

    private final TrackingDemandRepository trackingDemandRepository;
    private final DemandCodeGenerator demandCodeGenerator;

    public TrackingDemandServiceImpl(TrackingDemandRepository trackingDemandRepository,
                                     DemandCodeGenerator demandCodeGenerator) {
        this.trackingDemandRepository = trackingDemandRepository;
        this.demandCodeGenerator = demandCodeGenerator;
    }

    @Override
    public Page<TrackingDemandBO> page(TrackingDemandPageQuery query) {
        Page<TrackingDemand> page = trackingDemandRepository.page(query);
        List<TrackingDemandBO> list = page.getData().stream()
                .map(TrackingDemandAppConvert.INSTANCE::toTrackingDemandBO)
                .toList();
        return new Page<>(list, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    @Transactional
    public TrackingDemandBO create(TrackingDemandCmd cmd) {
        TrackingDemand demand = TrackingDemandAppConvert.INSTANCE.toTrackingDemand(cmd);
        demand.setDemandCode(demandCodeGenerator.generate());
        demand = demand.save(trackingDemandRepository);
        return TrackingDemandAppConvert.INSTANCE.toTrackingDemandBO(demand);
    }

    @Override
    @Transactional
    public TrackingDemandBO update(String id, TrackingDemandCmd cmd) {
        TrackingDemand existing = trackingDemandRepository.findById(id);
        Assert.notNull(existing, new SilentException("需求不存在"));

        TrackingDemand demand = TrackingDemandAppConvert.INSTANCE.toTrackingDemand(cmd);
        demand.setId(existing.getId());
        demand.setDemandCode(existing.getDemandCode());
        demand.setCreateBy(existing.getCreateBy());
        demand.setStatus(existing.getStatus());
        demand = demand.update(trackingDemandRepository);
        return TrackingDemandAppConvert.INSTANCE.toTrackingDemandBO(demand);
    }

    @Override
    public TrackingDemandBO detail(String id) {
        TrackingDemand demand = trackingDemandRepository.findById(id);
        Assert.notNull(demand, new SilentException("需求不存在"));
        return TrackingDemandAppConvert.INSTANCE.toTrackingDemandBO(demand);
    }

    @Override
    @Transactional
    public TrackingDemandBO submitDesign(String id) {
        TrackingDemand demand = trackingDemandRepository.findById(id);
        Assert.notNull(demand, new SilentException("需求不存在"));
        demand = demand.submitDesign(trackingDemandRepository);
        return TrackingDemandAppConvert.INSTANCE.toTrackingDemandBO(demand);
    }

    @Override
    @Transactional
    public TrackingDemandBO close(String id) {
        TrackingDemand demand = trackingDemandRepository.findById(id);
        Assert.notNull(demand, new SilentException("需求不存在"));
        demand = demand.close(trackingDemandRepository);
        return TrackingDemandAppConvert.INSTANCE.toTrackingDemandBO(demand);
    }
}

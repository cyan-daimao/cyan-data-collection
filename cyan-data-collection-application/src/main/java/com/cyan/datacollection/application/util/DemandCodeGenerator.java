package com.cyan.datacollection.application.util;

import com.cyan.datacollection.domain.demand.repository.TrackingDemandRepository;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 需求编号生成器
 * 格式：TD + yyyyMMdd + 4位序号
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Component
public class DemandCodeGenerator {

    private final TrackingDemandRepository trackingDemandRepository;

    private volatile String currentDate;
    private final AtomicInteger seq = new AtomicInteger(0);

    public DemandCodeGenerator(TrackingDemandRepository trackingDemandRepository) {
        this.trackingDemandRepository = trackingDemandRepository;
        this.currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    /**
     * 生成需求编号
     */
    public synchronized String generate() {
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        if (!today.equals(currentDate)) {
            currentDate = today;
            seq.set(0);
        }

        int maxSeq = trackingDemandRepository.findMaxSeqToday();
        int currentSeq = seq.incrementAndGet();
        if (currentSeq <= maxSeq) {
            currentSeq = maxSeq + 1;
            seq.set(currentSeq);
        }

        return "TD" + today + String.format("%04d", currentSeq);
    }
}

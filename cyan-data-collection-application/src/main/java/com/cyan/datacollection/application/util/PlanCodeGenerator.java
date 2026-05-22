package com.cyan.datacollection.application.util;

import com.cyan.datacollection.domain.plan.repository.TrackingPlanRepository;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 方案编号生成器
 * 格式：TP + yyyyMMdd + 4位序号
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Component
public class PlanCodeGenerator {

    private final TrackingPlanRepository trackingPlanRepository;

    private volatile String currentDate;
    private final AtomicInteger seq = new AtomicInteger(0);

    public PlanCodeGenerator(TrackingPlanRepository trackingPlanRepository) {
        this.trackingPlanRepository = trackingPlanRepository;
        this.currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    /**
     * 生成方案编号
     */
    public synchronized String generate() {
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        if (!today.equals(currentDate)) {
            currentDate = today;
            seq.set(0);
        }

        int maxSeq = trackingPlanRepository.findMaxSeqToday();
        int currentSeq = seq.incrementAndGet();
        if (currentSeq <= maxSeq) {
            currentSeq = maxSeq + 1;
            seq.set(currentSeq);
        }

        return "TP" + today + String.format("%04d", currentSeq);
    }
}

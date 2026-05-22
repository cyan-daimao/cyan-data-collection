package com.cyan.datacollection.application.util;

import com.cyan.datacollection.domain.release.repository.TrackingReleaseRepository;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 发布编号生成器
 * 格式：RL + yyyyMMdd + 3位序号
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Component
public class ReleaseCodeGenerator {

    private final TrackingReleaseRepository trackingReleaseRepository;

    private volatile String currentDate;
    private final AtomicInteger seq = new AtomicInteger(0);

    public ReleaseCodeGenerator(TrackingReleaseRepository trackingReleaseRepository) {
        this.trackingReleaseRepository = trackingReleaseRepository;
        this.currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    /**
     * 生成发布编号
     */
    public synchronized String generate() {
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        if (!today.equals(currentDate)) {
            currentDate = today;
            seq.set(0);
        }

        int maxSeq = trackingReleaseRepository.findMaxSeqToday();
        int currentSeq = seq.incrementAndGet();
        if (currentSeq <= maxSeq) {
            currentSeq = maxSeq + 1;
            seq.set(currentSeq);
        }

        return "RL" + today + String.format("%03d", currentSeq);
    }
}

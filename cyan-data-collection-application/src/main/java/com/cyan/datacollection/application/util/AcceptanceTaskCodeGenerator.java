package com.cyan.datacollection.application.util;

import com.cyan.datacollection.domain.acceptance.repository.TrackingAcceptanceTaskRepository;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 验收任务编号生成器
 * 格式：AT + yyyyMMdd + 3位序号
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Component
public class AcceptanceTaskCodeGenerator {

    private final TrackingAcceptanceTaskRepository trackingAcceptanceTaskRepository;

    private volatile String currentDate;
    private final AtomicInteger seq = new AtomicInteger(0);

    public AcceptanceTaskCodeGenerator(TrackingAcceptanceTaskRepository trackingAcceptanceTaskRepository) {
        this.trackingAcceptanceTaskRepository = trackingAcceptanceTaskRepository;
        this.currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    /**
     * 生成验收任务编号
     */
    public synchronized String generate() {
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        if (!today.equals(currentDate)) {
            currentDate = today;
            seq.set(0);
        }

        int maxSeq = trackingAcceptanceTaskRepository.findMaxSeqToday();
        int currentSeq = seq.incrementAndGet();
        if (currentSeq <= maxSeq) {
            currentSeq = maxSeq + 1;
            seq.set(currentSeq);
        }

        return "AT" + today + String.format("%03d", currentSeq);
    }
}

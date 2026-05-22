package com.cyan.datacollection.application.quality.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 采集链路状态业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LinkStatusBO {

    /**
     * 今日 HTTP 接收总量
     */
    private Long httpReceivedTotal;

    /**
     * 今日 HTTP 接收失败量
     */
    private Long httpFailedTotal;

    /**
     * 今日 Kafka 发送成功量
     */
    private Long kafkaSentTotal;

    /**
     * 今日 Kafka 发送失败量
     */
    private Long kafkaFailedTotal;

    /**
     * 今日 Debug 样本总量
     */
    private Long debugSampleTotal;

    /**
     * 统计时间
     */
    private LocalDateTime statTime;
}

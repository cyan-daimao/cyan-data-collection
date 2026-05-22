package com.cyan.datacollection.application.demand.bo;

import com.cyan.datacollection.enums.DemandStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 埋点需求业务对象
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingDemandBO {

    private String id;
    private String demandCode;
    private String demandName;
    private String businessDomain;
    private String productLine;
    private List<String> terminalTypes;
    private String priority;
    private String businessGoal;
    private String analysisGoal;
    private String productOwner;
    private String techOwner;
    private String testOwner;
    private String dataOwner;
    private LocalDate expectedReleaseDate;
    private DemandStatus status;
    private String createBy;
    private String updateBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

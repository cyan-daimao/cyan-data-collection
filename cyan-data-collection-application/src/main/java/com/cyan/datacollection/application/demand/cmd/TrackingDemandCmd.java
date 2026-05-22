package com.cyan.datacollection.application.demand.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * 埋点需求命令
 *
 * @author cy.Y
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackingDemandCmd {

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
    private String createBy;
    private String updateBy;
}

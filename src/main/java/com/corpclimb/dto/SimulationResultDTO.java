package com.corpclimb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResultDTO {
    private Long id;
    private Long projectId;
    private String projectName;
    private Integer numberOfTrials;
    private Double averageSuccessScore;
    private Double successProbability;
    private Double expectedSkillGain;
    private Double peerSynergyScore;
    private Double goalAlignmentScore;
    private Double confidenceIntervalLow;
    private Double confidenceIntervalHigh;
    private String recommendation;
    private LocalDateTime simulatedAt;
}
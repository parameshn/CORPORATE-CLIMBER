package com.corpclimb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSelectionDTO {
    private Long userId;
    private List<Long> candidateProjectIds;
    private Map<String, Double> priorityWeights; // SKILL_GROWTH, PEER_SYNERGY, GOAL_ALIGNMENT
    private Integer simulationTrials = 10000;
}
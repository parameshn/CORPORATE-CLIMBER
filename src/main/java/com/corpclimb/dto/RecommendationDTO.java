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
public class RecommendationDTO {
    private Long id;
    private Long userId;
    private Long simulationRunId;
    private String recommendationText;
    private String recommendationType;
    private Double confidence;
    private LocalDateTime createdAt;
}
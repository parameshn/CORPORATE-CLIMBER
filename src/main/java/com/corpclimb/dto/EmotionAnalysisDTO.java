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
public class EmotionAnalysisDTO {
    private Double sentimentScore;
    private String emotionCategory;
    private Double confidenceScore;
    private Double magnitude;
    private LocalDateTime analysisTimestamp;
}
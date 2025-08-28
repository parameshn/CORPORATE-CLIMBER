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
public class ProjectRequirementDTO {
    private Long id;
    private String skillName;
    private Integer requiredLevel;
    private Double importanceWeight;
    private LocalDateTime createdAt;
}

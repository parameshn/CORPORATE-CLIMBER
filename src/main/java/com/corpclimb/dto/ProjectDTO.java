package com.corpclimb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import com.corpclimb.entity.Project;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private String domain;
    private String priority;
    private Project.ProjectStatus status;
    private Integer durationMonths;
    private Double complexityScore;
    private Long managerId;
    private List<Long> peerIds;
    private List<ProjectRequirementDTO> requirements;
    private LocalDateTime createdAt;

    // public enum ProjectStatus {
    //     PLANNING, ACTIVE, ON_HOLD, COMPLETED, CANCELLED
    // }
}
package com.corpclimb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String department;
    private String role;
    private List<String> careerGoals;
    private List<UserSkillDTO> skills;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
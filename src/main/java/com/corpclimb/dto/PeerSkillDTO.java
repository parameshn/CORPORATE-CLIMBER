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
public class PeerSkillDTO {
    private Long id;
    private String skillName;
    private Integer skillLevel;
    private LocalDateTime lastUpdated;
}
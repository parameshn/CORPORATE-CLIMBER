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
public class PeerDTO {
    private Long id;
    private String name;
    private String email;
    private String department;
    private String role;
    private Integer skillLevel;
    private List<PeerSkillDTO> skills;
    private LocalDateTime createdAt;
}
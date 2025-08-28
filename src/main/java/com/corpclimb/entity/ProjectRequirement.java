package com.corpclimb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_requirements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(name = "required_level", nullable = false)
    private Integer requiredLevel; // 1-10

    @Column(name = "importance_weight", nullable = false)
    private Double importanceWeight; // 0.1-1.0

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
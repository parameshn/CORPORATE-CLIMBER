package com.corpclimb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "peers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Peer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String role;

    // Relationship to PeerSkills
    @OneToMany(mappedBy = "peer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PeerSkill> skills = new ArrayList<>();

    @Column(name = "skill_level", nullable = false)
    private Integer skillLevel; // 1-10 (overall skill level)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper method to add skill
    public void addSkill(PeerSkill skill) {
        skills.add(skill);
        skill.setPeer(this);
    }

    // Helper method to remove skill
    public void removeSkill(PeerSkill skill) {
        skills.remove(skill);
        skill.setPeer(null);
    }
}
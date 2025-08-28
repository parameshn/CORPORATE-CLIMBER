package com.corpclimb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "peer_skills")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeerSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "peer_id", nullable = false)
    private Peer peer;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(name = "skill_level", nullable = false)
    private Integer skillLevel; // 1-10 scale

    @Column(name = "verified_by")
    private String verifiedBy; // Name/ID of verifier

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        lastUpdated = LocalDateTime.now();
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}
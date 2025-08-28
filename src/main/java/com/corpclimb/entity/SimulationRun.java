package com.corpclimb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "simulation_runs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "number_of_trials", nullable = false)
    private Integer numberOfTrials;

    @Column(name = "average_success_score", nullable = false)
    private Double averageSuccessScore;

    @Column(name = "success_probability", nullable = false)
    private Double successProbability;

    @Column(name = "expected_skill_gain", nullable = false)
    private Double expectedSkillGain;

    @Column(name = "peer_synergy_score", nullable = false)
    private Double peerSynergyScore;

    @Column(name = "goal_alignment_score", nullable = false)
    private Double goalAlignmentScore;

    @Column(name = "confidence_interval_low", nullable = false)
    private Double confidenceIntervalLow;

    @Column(name = "confidence_interval_high", nullable = false)
    private Double confidenceIntervalHigh;

    @Column(name = "simulated_at", nullable = false)
    private LocalDateTime simulatedAt;

    @PrePersist
    protected void onCreate() {
        simulatedAt = LocalDateTime.now();
    }
}
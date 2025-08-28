package com.corpclimb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "emotion_scores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmotionScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private ConversationRecord conversation;

    @Column(name = "sentiment_score", nullable = false)
    private Double sentimentScore; // -1.0 to 1.0

    @Column(name = "emotion_category", nullable = false)
    private String emotionCategory; // JOY, ANGER, FEAR, SADNESS

    @Column(name = "confidence_score", nullable = false)
    private Double confidenceScore; // 0.0 to 1.0

    @Column(nullable = false)
    private Double magnitude; // 0.0+

    @Column(name = "analysis_timestamp", nullable = false)
    private LocalDateTime analysisTimestamp;

    @PrePersist
    protected void onCreate() {
        analysisTimestamp = LocalDateTime.now();
    }
}
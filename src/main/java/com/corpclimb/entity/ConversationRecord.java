package com.corpclimb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "conversation_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "peer_id")
    private Peer peer;

    @Column(name = "conversation_text", nullable = false, columnDefinition = "TEXT")
    private String conversationText;

    @Column(nullable = false)
    private String platform; // SLACK, TEAMS, EMAIL, MEETING

    @Column(name = "conversation_date", nullable = false)
    private LocalDateTime conversationDate;

    @Column(nullable = false)
    private Boolean processed = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
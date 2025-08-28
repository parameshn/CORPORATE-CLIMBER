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
public class PeerInteractionDTO {
    private Long peerId;
    private String peerName;
    private Double averageSentiment;
    private Integer interactionFrequency;
    private Double synergyScore;
    private LocalDateTime lastInteraction;
}
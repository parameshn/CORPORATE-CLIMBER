package com.corpclimb.service;

import com.corpclimb.entity.*;
import com.corpclimb.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.*;
import java.util.Map;
import com.corpclimb.dto.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataAggregationService {

    private final UserSkillRepository userSkillRepository;
    private final EmotionScoreRepository emotionScoreRepository;
    private final ConversationRecordRepository conversationRepository;
    private final ProjectRequirementRepository requirementRepository;
    private final PeerRepository peerRepository;

    @Transactional(readOnly = true)
    public Map<String, Double> getUserSkillVector(Long userId) {
        List<UserSkill> skills = userSkillRepository.findByUserId(userId);

        return skills.stream()
                .collect(Collectors.toMap(
                        UserSkill::getSkillName,
                        skill -> (double) skill.getCurrentLevel(),
                        (existing, replacement) -> Math.max(existing, replacement)));
    }

    @Transactional(readOnly = true)
    public Map<Long, Double> getPeerInteractionScores(Long userId) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<ConversationRecord> conversations = conversationRepository
                .findByUserIdAndConversationDateAfter(userId, oneMonthAgo);

        Map<Long, Double> interactionScores = new HashMap<>();

        for (ConversationRecord conversation : conversations) {
            List<EmotionScore> emotions = emotionScoreRepository
                    .findByConversationId(conversation.getId());

            if (!emotions.isEmpty()) {
                double avgSentiment = emotions.stream()
                        .mapToDouble(EmotionScore::getSentimentScore)
                        .average()
                        .orElse(0.0);

                Long peerId = conversation.getPeer().getId();
                interactionScores.merge(peerId, avgSentiment, (existing, newScore) -> (existing + newScore) / 2.0);
            }
        }

        return interactionScores;
    }

    @Transactional(readOnly = true)
    public Map<String, Double> getProjectRequirementVector(Long projectId) {
        List<ProjectRequirement> requirements = requirementRepository
                .findByProjectId(projectId);

        return requirements.stream()
                .collect(Collectors.toMap(
                        ProjectRequirement::getSkillName,
                        req -> (double) req.getRequiredLevel() * req.getImportanceWeight()));
    }

    @Transactional(readOnly = true)
    public double calculateSkillGap(Map<String, Double> userSkills,
            Map<String, Double> projectRequirements) {
        double totalGap = 0.0;
        int skillCount = 0;

        for (Map.Entry<String, Double> requirement : projectRequirements.entrySet()) {
            String skill = requirement.getKey();
            double required = requirement.getValue();
            double current = userSkills.getOrDefault(skill, 0.0);

            if (current < required) {
                totalGap += (required - current);
                skillCount++;
            }
        }

        return skillCount > 0 ? totalGap / skillCount : 0.0;
    }

    @Transactional(readOnly = true)
    public List<PeerInteractionDTO> getPeerInteractions(Long userId) {
        Map<Long, Double> interactionScores = getPeerInteractionScores(userId);
        List<PeerInteractionDTO> interactions = new ArrayList<>();

        for (Map.Entry<Long, Double> entry : interactionScores.entrySet()) {
            Peer peer = peerRepository.findById(entry.getKey())
                    .orElse(null);

            if (peer != null) {
                long frequency = conversationRepository.countByUserIdAndPeerId(userId, peer.getId());

                interactions.add(PeerInteractionDTO.builder()
                        .peerId(peer.getId())
                        .peerName(peer.getName())
                        .averageSentiment(entry.getValue())
                        .interactionFrequency((int) frequency)
                        .synergyScore((entry.getValue() + 1) / 2 * 10) // Scale to 0-10
                        .lastInteraction(conversationRepository
                                .findTopByUserIdAndPeerIdOrderByConversationDateDesc(userId, peer.getId())
                                .map(ConversationRecord::getConversationDate) // Corrected line
                                .orElse(null))
                        .build());
            }
        }

        return interactions;
    }
}
package com.corpclimb.service;

import com.corpclimb.config.AppConfig;
import com.corpclimb.dto.RecommendationDTO;
import com.corpclimb.entity.Recommendation;
import com.corpclimb.entity.SimulationRun;
import com.corpclimb.entity.User;
import com.corpclimb.repository.RecommendationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import com.corpclimb.exceptions.ServiceException;
import com.corpclimb.entity.Project;
import com.corpclimb.exceptions.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final AppConfig appConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public String generateRecommendation(SimulationRun simulationRun) {
        try {
            String prompt = buildPrompt(simulationRun);
            String recommendation = callGeminiAPI(prompt);
            saveRecommendation(simulationRun, recommendation);
            return recommendation;
        } catch (Exception e) {
            log.error("Error generating recommendation for simulation run {}", simulationRun.getId(), e);
            return generateFallbackRecommendation(simulationRun);
        }
    }

    private void saveRecommendation(SimulationRun simulationRun, String recommendation) {
        Recommendation rec = Recommendation.builder()
                .user(simulationRun.getUser())
                .simulationRun(simulationRun)
                .recommendationText(recommendation)
                .recommendationType(determineRecommendationType(simulationRun))
                .confidence(simulationRun.getSuccessProbability())
                .build();

        recommendationRepository.save(rec);
    }

    private String buildPrompt(SimulationRun simulationRun) {
        User user = simulationRun.getUser();
        Project project = simulationRun.getProject();

        StringBuilder prompt = new StringBuilder();
        prompt.append("As a corporate career advisor, analyze this project fit:\n\n");
        prompt.append("### Employee Profile ###\n");
        prompt.append("Name: ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("\n");
        prompt.append("Department: ").append(user.getDepartment()).append("\n");
        prompt.append("Role: ").append(user.getRole()).append("\n");
        prompt.append("Career Goals: ").append(String.join(", ", user.getCareerGoals())).append("\n\n");

        prompt.append("### Project Details ###\n");
        prompt.append("Name: ").append(project.getName()).append("\n");
        prompt.append("Domain: ").append(project.getDomain()).append("\n");
        prompt.append("Duration: ").append(project.getDurationMonths()).append(" months\n");
        prompt.append("Complexity: ").append(project.getComplexityScore()).append("/10\n\n");

        prompt.append("### Simulation Results ###\n");
        prompt.append("Success Probability: ").append(String.format("%.1f%%",
                simulationRun.getSuccessProbability() * 100)).append("\n");
        prompt.append("Expected Skill Growth: +").append(String.format("%.1f levels",
                simulationRun.getExpectedSkillGain())).append("\n");
        prompt.append("Peer Synergy: ").append(String.format("%.1f/10",
                simulationRun.getPeerSynergyScore() * 10)).append("\n");
        prompt.append("Goal Alignment: ").append(String.format("%.1f/10",
                simulationRun.getGoalAlignmentScore() * 10)).append("\n\n");

        prompt.append("### Recommendation Request ###\n");
        prompt.append("Provide a concise recommendation (2-3 sentences) covering:\n");
        prompt.append("1. Project suitability for career growth\n");
        prompt.append("2. Key strengths and risks\n");
        prompt.append("3. Suggested preparation steps\n");
        prompt.append("Use a professional but encouraging tone.");

        return prompt.toString();
    }

    private String callGeminiAPI(String prompt) {
        try {
            String url = appConfig.getGeminiBaseUrl() + "/models/gemini-pro:generateContent?key=" +
                    appConfig.getGeminiApiKey();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(Map.of(
                            "parts", List.of(Map.of("text", prompt)))));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("candidates").get(0)
                        .path("content").path("parts").get(0)
                        .path("text").asText();
            }

            throw new ServiceException("Gemini API returned status: " + response.getStatusCode());
        } catch (Exception e) {
            log.error("Gemini API call failed", e);
            throw new ServiceException("Failed to call Gemini API", e);
        }
    }

    private String determineRecommendationType(SimulationRun simulationRun) {
        double prob = simulationRun.getSuccessProbability();
        if (prob >= 0.75)
            return "PRIMARY";
        if (prob >= 0.6)
            return "ALTERNATIVE";
        return "CAUTION";
    }

    public List<RecommendationDTO> getUserRecommendations(Long userId) {
        return recommendationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public RecommendationDTO getRecommendationById(Long id) {
        return recommendationRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Recommendation not found"));
    }

    private String generateFallbackRecommendation(SimulationRun simulationRun) {
        double prob = simulationRun.getSuccessProbability();
        String projectName = simulationRun.getProject().getName();

        if (prob >= 0.75) {
            return "Based on our analysis, the '" + projectName
                    + "' project is an excellent match for your skills and career goals. "
                    + "With a high success probability of " + String.format("%.0f%%", prob * 100)
                    + ", this opportunity offers strong "
                    + "potential for professional growth. We recommend discussing this project with your manager as a priority.";
        } else if (prob >= 0.6) {
            return "The '" + projectName + "' project presents a good growth opportunity with moderate challenges. "
                    + "Your success probability is " + String.format("%.0f%%", prob * 100)
                    + ", indicating you may need to develop "
                    + "some skills during the project. Consider identifying key areas for development and seeking mentorship.";
        } else {
            return "The '" + projectName + "' project may be challenging given your current skill set ("
                    + String.format("%.0f%%", prob * 100)
                    + " success probability). While it aligns with some career goals, "
                    + "we recommend building foundational skills first or exploring alternative projects with better fit.";
        }
    }

    private RecommendationDTO mapToDTO(Recommendation rec) {
        return RecommendationDTO.builder()
                .id(rec.getId())
                .userId(rec.getUser().getId())
                .simulationRunId(rec.getSimulationRun().getId())
                .recommendationText(rec.getRecommendationText())
                .recommendationType(rec.getRecommendationType())
                .confidence(rec.getConfidence())
                .createdAt(rec.getCreatedAt())
                .build();
    }
}
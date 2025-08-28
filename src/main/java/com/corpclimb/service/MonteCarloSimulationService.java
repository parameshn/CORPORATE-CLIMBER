package com.corpclimb.service;

import com.corpclimb.dto.ProjectSelectionDTO;
import com.corpclimb.dto.SimulationResultDTO;
import com.corpclimb.entity.*;
import com.corpclimb.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import com.corpclimb.exceptions.EntityNotFoundException;
import lombok.Data;
import lombok.Builder;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonteCarloSimulationService {

    private final SimulationRunRepository simulationRunRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final DataAggregationService dataService;
    private final RecommendationService recommendationService;

    private static final int DEFAULT_TRIALS = 10000;
    private static final double SKILL_GROWTH_FACTOR = 0.15;

    @Transactional
    public List<SimulationResultDTO> simulateProjectSelection(ProjectSelectionDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Project> candidateProjects = projectRepository.findAllById(dto.getCandidateProjectIds());
        int trials = dto.getSimulationTrials() != null ? dto.getSimulationTrials() : DEFAULT_TRIALS;

        // Get weights with defaults
        double skillWeight = dto.getPriorityWeights().getOrDefault("SKILL_GROWTH", 0.4);
        double peerWeight = dto.getPriorityWeights().getOrDefault("PEER_SYNERGY", 0.25);
        double goalWeight = dto.getPriorityWeights().getOrDefault("GOAL_ALIGNMENT", 0.35);

        // Get user data vectors
        Map<String, Double> userSkills = dataService.getUserSkillVector(dto.getUserId());
        Map<Long, Double> peerInteractionScores = dataService.getPeerInteractionScores(dto.getUserId());

        return candidateProjects.parallelStream()
                .map(project -> runMonteCarloSimulation(user, project, userSkills,
                        peerInteractionScores, trials,
                        skillWeight, peerWeight, goalWeight))
                .sorted(Comparator.comparingDouble(SimulationResultDTO::getSuccessProbability).reversed())
                .collect(Collectors.toList());
    }

    private SimulationResultDTO runMonteCarloSimulation(User user, Project project,
            Map<String, Double> userSkills,
            Map<Long, Double> peerInteractionScores,
            int trials,
            double skillWeight,
            double peerWeight,
            double goalWeight) {

        Map<String, Double> projectRequirements = dataService.getProjectRequirementVector(project.getId());

        List<Double> successScores = new ArrayList<>(trials);
        List<Double> skillGains = new ArrayList<>(trials);
        List<Double> synergyScores = new ArrayList<>(trials);
        List<Double> goalAlignmentScores = new ArrayList<>(trials);

        // Run Monte Carlo trials
        for (int trial = 0; trial < trials; trial++) {
            SimulationTrial trialResult = runSingleTrial(user, project, userSkills,
                    projectRequirements, peerInteractionScores,
                    skillWeight, peerWeight, goalWeight);

            successScores.add(trialResult.getSuccessScore());
            skillGains.add(trialResult.getSkillGain());
            synergyScores.add(trialResult.getSynergyScore());
            goalAlignmentScores.add(trialResult.getGoalAlignmentScore());
        }

        // Calculate statistics
        double avgSuccessScore = calculateAverage(successScores);
        double successProbability = successScores.stream()
                .mapToDouble(s -> s > 0.7 ? 1.0 : 0.0)
                .average().orElse(0.0);
        double avgSkillGain = calculateAverage(skillGains);
        double avgSynergyScore = calculateAverage(synergyScores);
        double avgGoalAlignment = calculateAverage(goalAlignmentScores);

        // Calculate confidence intervals
        Collections.sort(successScores);
        double confidenceLow = successScores.get((int) (trials * 0.025));
        double confidenceHigh = successScores.get((int) (trials * 0.975));

        // Save simulation run
        SimulationRun simulationRun = SimulationRun.builder()
                .user(user)
                .project(project)
                .numberOfTrials(trials)
                .averageSuccessScore(avgSuccessScore)
                .successProbability(successProbability)
                .expectedSkillGain(avgSkillGain)
                .peerSynergyScore(avgSynergyScore)
                .goalAlignmentScore(avgGoalAlignment)
                .confidenceIntervalLow(confidenceLow)
                .confidenceIntervalHigh(confidenceHigh)
                .build();

        SimulationRun savedRun = simulationRunRepository.save(simulationRun);

        // Generate recommendation
        String recommendation = recommendationService.generateRecommendation(savedRun);

        return SimulationResultDTO.builder()
                .id(savedRun.getId())
                .projectId(project.getId())
                .projectName(project.getName())
                .numberOfTrials(trials)
                .averageSuccessScore(avgSuccessScore)
                .successProbability(successProbability)
                .expectedSkillGain(avgSkillGain)
                .peerSynergyScore(avgSynergyScore)
                .goalAlignmentScore(avgGoalAlignment)
                .confidenceIntervalLow(confidenceLow)
                .confidenceIntervalHigh(confidenceHigh)
                .recommendation(recommendation)
                .simulatedAt(savedRun.getSimulatedAt())
                .build();
    }

    private double calculateAverage(List<Double> values) {
        return values.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private SimulationTrial runSingleTrial(User user, Project project,
            Map<String, Double> userSkills,
            Map<String, Double> projectRequirements,
            Map<Long, Double> peerInteractionScores,
            double skillWeight,
            double peerWeight,
            double goalWeight) {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        // Calculate skill match with noise
        double skillMatch = calculateSkillMatch(userSkills, projectRequirements);
        double noisySkillMatch = addNoise(skillMatch, 0.1);

        // Calculate peer synergy
        double peerSynergy = calculatePeerSynergy(project, peerInteractionScores);
        double noisyPeerSynergy = addNoise(peerSynergy, 0.15);

        // Calculate goal alignment
        double goalAlignment = calculateGoalAlignment(user, project);
        double noisyGoalAlignment = addNoise(goalAlignment, 0.1);

        // Project difficulty factor
        double difficultyFactor = 1.0 - (project.getComplexityScore() / 10.0);
        double noisyDifficulty = addNoise(difficultyFactor, 0.2, 0.1, 1.0);

        // Calculate composite success score
        double successScore = (noisySkillMatch * skillWeight +
                noisyPeerSynergy * peerWeight +
                noisyGoalAlignment * goalWeight +
                noisyDifficulty * 0.05);

        // Calculate expected skill gain
        double skillGap = dataService.calculateSkillGap(userSkills, projectRequirements);
        double skillGain = skillGap * SKILL_GROWTH_FACTOR * (project.getDurationMonths() / 12.0);

        return SimulationTrial.builder()
                .successScore(successScore)
                .skillGain(skillGain)
                .synergyScore(noisyPeerSynergy)
                .goalAlignmentScore(noisyGoalAlignment)
                .build();
    }

    private double addNoise(double value, double stdDev) {
        return addNoise(value, stdDev, 0.0, 1.0);
    }

    private double addNoise(double value, double stdDev, double min, double max) {
        double noisyValue = value + ThreadLocalRandom.current().nextGaussian() * stdDev;
        return Math.max(min, Math.min(max, noisyValue));
    }

    // Calculate how well user skills match project requirements
    private double calculateSkillMatch(Map<String, Double> userSkills,
            Map<String, Double> projectRequirements) {
        if (projectRequirements.isEmpty())
            return 0.5; // Default value for no requirements

        double totalMatch = 0.0;
        int count = 0;

        for (Map.Entry<String, Double> req : projectRequirements.entrySet()) {
            String skill = req.getKey();
            double requiredLevel = req.getValue();
            double userLevel = userSkills.getOrDefault(skill, 0.0);

            // Cap match at 1.0 (100%)
            double match = Math.min(userLevel / requiredLevel, 1.0);
            totalMatch += match;
            count++;
        }

        return count > 0 ? totalMatch / count : 0.5;
    }

    // Calculate synergy with peers in the project
    private double calculatePeerSynergy(Project project,
            Map<Long, Double> peerInteractionScores) {
        if (project.getAssignedPeers().isEmpty())
            return 0.5; // Default value for no peers

        double totalSynergy = 0.0;
        int peerCount = 0;

        for (Peer peer : project.getAssignedPeers()) {
            double sentiment = peerInteractionScores.getOrDefault(peer.getId(), 0.0);
            // Convert sentiment (-1 to 1) to synergy (0 to 1)
            double synergy = (sentiment + 1) / 2.0;
            totalSynergy += synergy;
            peerCount++;
        }

        return peerCount > 0 ? totalSynergy / peerCount : 0.5;
    }

    // Calculate alignment with user's career goals
    private double calculateGoalAlignment(User user, Project project) {
        if (user.getCareerGoals() == null || user.getCareerGoals().isEmpty())
            return 0.5;

        double alignmentScore = 0.0;
        String projectText = (project.getName() + " " + project.getDescription() + " " + project.getDomain())
                .toLowerCase();

        for (String goal : user.getCareerGoals()) {
            String normalizedGoal = goal.toLowerCase();
            if (projectText.contains(normalizedGoal)) {
                alignmentScore += 0.3; // Add 0.3 for each matching goal
            }
        }

        // Cap at 1.0 (100% alignment)
        return Math.min(alignmentScore, 1.0);
    }

    public List<SimulationResultDTO> getUserSimulations(Long userId) {
        return simulationRunRepository.findByUserIdOrderBySimulatedAtDesc(userId).stream()
                .map(this::mapToSimulationResultDTO)
                .collect(Collectors.toList());
    }

    private SimulationResultDTO mapToSimulationResultDTO(SimulationRun run) {
        return SimulationResultDTO.builder()
                .id(run.getId())
                .projectId(run.getProject().getId())
                .projectName(run.getProject().getName())
                .numberOfTrials(run.getNumberOfTrials())
                .averageSuccessScore(run.getAverageSuccessScore())
                .successProbability(run.getSuccessProbability())
                .expectedSkillGain(run.getExpectedSkillGain())
                .peerSynergyScore(run.getPeerSynergyScore())
                .goalAlignmentScore(run.getGoalAlignmentScore())
                .confidenceIntervalLow(run.getConfidenceIntervalLow())
                .confidenceIntervalHigh(run.getConfidenceIntervalHigh())
                .simulatedAt(run.getSimulatedAt())
                .build();
    }

    @Data
    @Builder
    private static class SimulationTrial {
        private double successScore;
        private double skillGain;
        private double synergyScore;
        private double goalAlignmentScore;
    }
}
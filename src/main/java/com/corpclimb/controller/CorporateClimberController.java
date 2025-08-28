package com.corpclimb.controller;

import com.corpclimb.dto.*;
import com.corpclimb.entity.Project;
import com.corpclimb.entity.Recommendation;
import com.corpclimb.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CorporateClimberController {

    // Service dependencies
    private final UserProfileService userProfileService;
    private final ProjectService projectService;
    private final EmotionAnalysisService emotionAnalysisService;
    private final MonteCarloSimulationService simulationService;
    private final RecommendationService recommendationService;
    private final PeerService peerService;
    private final DataAggregationService dataService;

    // ================================
    // 1. USER PROFILE ENDPOINTS
    // ================================

    @PostMapping("/users")
    public ResponseEntity<UserProfileDTO> createUser(@Valid @RequestBody UserProfileDTO userDto) {
        log.info("Creating user profile for: {}", userDto.getEmail());
        UserProfileDTO created = userProfileService.createUserProfile(userDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserProfileDTO> getUser(@PathVariable Long userId) {
        log.info("Fetching user profile: {}", userId);
        UserProfileDTO user = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

    // @PutMapping("/users/{userId}/skills")
    // public ResponseEntity<UserProfileDTO> updateUserSkills(
    //         @PathVariable Long userId,
    //         @RequestBody List<UserSkillDTO> skills) {
    //     log.info("Updating skills for user: {}", userId);
    //     UserProfileDTO updated = userProfileService.updateUserSkills(userId, skills);
    //     return ResponseEntity.ok(updated);
    // }

    // ================================
    // 2. PEER ENDPOINTS
    // ================================

    @GetMapping("/peers/department/{department}")
    public ResponseEntity<List<PeerDTO>> getPeersByDepartment(
            @PathVariable String department) {
        log.info("Fetching peers in department: {}", department);
        List<PeerDTO> peers = peerService.getPeersByDepartment(department);
        return ResponseEntity.ok(peers);
    }

    @PostMapping("/peers/{peerId}/skills")
    public ResponseEntity<PeerDTO> addPeerSkill(
            @PathVariable Long peerId,
            @Valid @RequestBody PeerSkillDTO skillDTO) {
        log.info("Adding skill to peer: {}", peerId);
        PeerDTO updated = peerService.addSkillToPeer(peerId, skillDTO);
        return ResponseEntity.ok(updated);
    }

    // ================================
    // 3. PROJECT ENDPOINTS
    // ================================

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        log.info("Fetching all projects");
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long projectId) {
        log.info("Fetching project: {}", projectId);
        ProjectDTO project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/projects/domain/{domain}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByDomain(
            @PathVariable String domain) {
        log.info("Fetching projects in domain: {}", domain);
        List<ProjectDTO> projects = projectService.getProjectsByDomain(domain);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectDTO> createProject(
            @Valid @RequestBody ProjectDTO projectDTO) {
        log.info("Creating project: {}", projectDTO.getName());
        ProjectDTO created = projectService.createProject(projectDTO);
        return ResponseEntity.ok(created);
    }

    // ================================
    // 4. CONVERSATION ANALYSIS ENDPOINTS
    // ================================

    @PostMapping("/conversations")
    public ResponseEntity<EmotionAnalysisDTO> uploadConversation(
            @Valid @RequestBody ConversationUploadDTO conversationDto) {
        log.info("Analyzing conversation for user: {} with peer: {}",
                conversationDto.getUserId(), conversationDto.getPeerId());
        EmotionAnalysisDTO analysis = emotionAnalysisService.analyzeConversation(conversationDto);
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/users/{userId}/interactions")
    public ResponseEntity<List<PeerInteractionDTO>> getPeerInteractions(
            @PathVariable Long userId) {
        log.info("Fetching peer interactions for user: {}", userId);
        List<PeerInteractionDTO> interactions = dataService.getPeerInteractions(userId);
        return ResponseEntity.ok(interactions);
    }

    // ================================
    // 5. SIMULATION ENDPOINTS
    // ================================

    @PostMapping("/simulate")
    public ResponseEntity<List<SimulationResultDTO>> simulateProjectSelection(
            @Valid @RequestBody ProjectSelectionDTO selectionDto) {
        log.info("Running simulation for user: {} with {} projects",
                selectionDto.getUserId(), selectionDto.getCandidateProjectIds().size());
        List<SimulationResultDTO> results = simulationService.simulateProjectSelection(selectionDto);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/users/{userId}/simulations")
    public ResponseEntity<List<SimulationResultDTO>> getUserSimulations(
            @PathVariable Long userId) {
        log.info("Fetching simulations for user: {}", userId);
        List<SimulationResultDTO> simulations = simulationService.getUserSimulations(userId);
        return ResponseEntity.ok(simulations);
    }

    // ================================
    // 6. RECOMMENDATION ENDPOINTS
    // ================================

    @GetMapping("/users/{userId}/recommendations")
    public ResponseEntity<List<RecommendationDTO>> getRecommendations(
            @PathVariable Long userId) {
        log.info("Fetching recommendations for user: {}", userId);
        List<RecommendationDTO> recommendations = recommendationService.getUserRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/recommendations/{id}")
    public ResponseEntity<RecommendationDTO> getRecommendation(
            @PathVariable Long id) {
        log.info("Fetching recommendation: {}", id);
        RecommendationDTO recommendation = recommendationService.getRecommendationById(id);
        return ResponseEntity.ok(recommendation);
    }

    // ================================
    // 7. HEALTH CHECK ENDPOINT
    // ================================

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Corporate Climber API is healthy");
    }

    // ================================
    // 8. DATA ENDPOINTS
    // ================================

    @GetMapping("/users/{userId}/skill-vector")
    public ResponseEntity<Map<String, Double>> getUserSkillVector(
            @PathVariable Long userId) {
        log.info("Fetching skill vector for user: {}", userId);
        Map<String, Double> skills = dataService.getUserSkillVector(userId);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/projects/{projectId}/requirement-vector")
    public ResponseEntity<Map<String, Double>> getProjectRequirementVector(
            @PathVariable Long projectId) {
        log.info("Fetching requirement vector for project: {}", projectId);
        Map<String, Double> requirements = dataService.getProjectRequirementVector(projectId);
        return ResponseEntity.ok(requirements);
    }
}
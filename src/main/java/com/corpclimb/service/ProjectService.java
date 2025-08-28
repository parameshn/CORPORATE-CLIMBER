package com.corpclimb.service;

import com.corpclimb.dto.*;
import com.corpclimb.entity.*;
import com.corpclimb.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import com.corpclimb.exceptions.*;
import java.util.Collections;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectRequirementRepository requirementRepository;
    private final PeerRepository peerRepository;
    private final ManagerRepository managerRepository;

    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        return mapToProjectDTO(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByDomain(String domain) {
            return projectRepository.findByDomain(domain)
                            .stream()
                            .map(this::mapToProjectDTO)
                            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProjectDTO> getAllProjects() {
            return projectRepository.findAll().stream()
                            .map(this::mapToProjectDTO)
                            .collect(Collectors.toList());
    }

    private ProjectDTO mapToProjectDTO(Project project) {
        List<ProjectRequirementDTO> requirements = requirementRepository.findByProjectId(project.getId())
                .stream()
                .map(this::mapToRequirementDTO)
                .collect(Collectors.toList());

        List<Long> peerIds = project.getAssignedPeers().stream()
                .map(Peer::getId)
                .collect(Collectors.toList());

        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .domain(project.getDomain())
                .priority(project.getPriority())
                .status(project.getStatus())
                .durationMonths(project.getDurationMonths())
                .complexityScore(project.getComplexityScore())
                .managerId(project.getManager() != null ? project.getManager().getId() : null)
                .peerIds(peerIds)
                .requirements(requirements)
                .createdAt(project.getCreatedAt())
                .build();
    }

    private ProjectRequirementDTO mapToRequirementDTO(ProjectRequirement requirement) {
        return ProjectRequirementDTO.builder()
                .id(requirement.getId())
                .skillName(requirement.getSkillName())
                .requiredLevel(requirement.getRequiredLevel())
                .importanceWeight(requirement.getImportanceWeight())
                .createdAt(requirement.getCreatedAt())
                .build();
    }

    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Manager manager = projectDTO.getManagerId() != null ? managerRepository.findById(projectDTO.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("Manager not found")) : null;

        List<Peer> peers = projectDTO.getPeerIds() != null ? peerRepository.findAllById(projectDTO.getPeerIds())
                : Collections.emptyList();

        Project project = Project.builder()
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .domain(projectDTO.getDomain())
                .priority(projectDTO.getPriority())
                .status(projectDTO.getStatus())
                .durationMonths(projectDTO.getDurationMonths())
                .complexityScore(projectDTO.getComplexityScore())
                .manager(manager)
                .assignedPeers(peers)
                .build();

        Project savedProject = projectRepository.save(project);

        // Save project requirements
        if (projectDTO.getRequirements() != null) {
            projectDTO.getRequirements().forEach(reqDTO -> {
                ProjectRequirement requirement = ProjectRequirement.builder()
                        .project(savedProject)
                        .skillName(reqDTO.getSkillName())
                        .requiredLevel(reqDTO.getRequiredLevel())
                        .importanceWeight(reqDTO.getImportanceWeight())
                        .build();
                requirementRepository.save(requirement);
            });
        }

        return mapToProjectDTO(savedProject);
    }
}
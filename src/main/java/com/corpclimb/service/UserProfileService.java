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
import com.corpclimb.exceptions.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserSkillRepository userSkillRepository;

    @Transactional
    public UserProfileDTO createUserProfile(UserProfileDTO dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .department(dto.getDepartment())
                .role(dto.getRole())
                .careerGoals(dto.getCareerGoals())
                .build();

        User savedUser = userRepository.save(user);

        // Save user skills
        if (dto.getSkills() != null) {
            dto.getSkills().forEach(skillDto -> {
                UserSkill skill = UserSkill.builder()
                        .user(savedUser)
                        .skillName(skillDto.getSkillName())
                        .currentLevel(skillDto.getCurrentLevel())
                        .targetLevel(skillDto.getTargetLevel())
                        .build();
                userSkillRepository.save(skill);
            });
        }

        return getUserProfile(savedUser.getId());
    }

    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(Long userId) {
            User user = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

            List<UserSkillDTO> skills = userSkillRepository.findByUserId(userId)
                            .stream()
                            .map(this::mapToUserSkillDTO)
                            .collect(Collectors.toList());

            return UserProfileDTO.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .department(user.getDepartment())
                            .role(user.getRole())
                            .careerGoals(user.getCareerGoals())
                            .skills(skills)
                            .createdAt(user.getCreatedAt())
                            .updatedAt(user.getUpdatedAt())
                            .build();
    }
    
    @Transactional
    public UserSkillDTO addSkillToUser(Long userId, UserSkillDTO skillDTO) {
            User user = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

            UserSkill skill = UserSkill.builder()
                            .user(user)
                            .skillName(skillDTO.getSkillName())
                            .currentLevel(skillDTO.getCurrentLevel())
                            .targetLevel(skillDTO.getTargetLevel())
                            .build();

            UserSkill savedSkill = userSkillRepository.save(skill);
            return mapToUserSkillDTO(savedSkill);
    }

    private UserSkillDTO mapToUserSkillDTO(UserSkill skill) {
        return UserSkillDTO.builder()
                .id(skill.getId())
                .skillName(skill.getSkillName())
                .currentLevel(skill.getCurrentLevel())
                .targetLevel(skill.getTargetLevel())
                .lastAssessed(skill.getLastAssessed())
                .createdAt(skill.getCreatedAt())
                .build();
    }

    
}
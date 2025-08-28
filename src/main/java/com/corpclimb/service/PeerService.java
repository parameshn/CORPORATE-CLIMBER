package com.corpclimb.service;


import java.util.List;
import com.corpclimb.repository.*;
import com.corpclimb.exceptions.*;
import com.corpclimb.entity.*;
import com.corpclimb.dto.*;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeerService {
    private final PeerRepository peerRepository;
    private final PeerSkillRepository peerSkillRepository;
    

    @Transactional
    public PeerDTO addSkillToPeer(Long peerId, PeerSkillDTO skillDTO) {
        Peer peer = peerRepository.findById(peerId)
                .orElseThrow(() -> new EntityNotFoundException("Peer not found"));

        PeerSkill skill = PeerSkill.builder()
                .peer(peer)
                .skillName(skillDTO.getSkillName())
                .skillLevel(skillDTO.getSkillLevel())
                .build();

        peerSkillRepository.save(skill);
        return mapToPeerDTO(peer);
    }
    
    public List<PeerDTO> getPeersByDepartment(String department) {
        return peerRepository.findByDepartment(department).stream()
                .map(this::mapToPeerDTO)
                .collect(Collectors.toList());
    }

    private PeerDTO mapToPeerDTO(Peer peer) {
        List<PeerSkillDTO> skills = peer.getSkills().stream()
            .map(this::mapToSkillDTO)
            .collect(Collectors.toList());
        
        return PeerDTO.builder()
            .id(peer.getId())
            .name(peer.getName())
            .email(peer.getEmail())
            .department(peer.getDepartment())
            .role(peer.getRole())
            .skillLevel(peer.getSkillLevel())
            .skills(skills)
            .createdAt(peer.getCreatedAt())
            .build();
    }

    private PeerSkillDTO mapToSkillDTO(PeerSkill skill) {
        return PeerSkillDTO.builder()
                .id(skill.getId())
                .skillName(skill.getSkillName())
                .skillLevel(skill.getSkillLevel())
                .lastUpdated(skill.getLastUpdated())
                .build();
    }
    
}
package com.corpclimb.repository;

import com.corpclimb.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeerSkillRepository extends JpaRepository<PeerSkill, Long> {
    @Query("SELECT ps FROM PeerSkill ps WHERE ps.peer.id = :peerId")
    List<PeerSkill> findByPeerId(@Param("peerId") Long peerId);

    @Query("SELECT ps FROM PeerSkill ps WHERE ps.skillName = :skillName AND ps.skillLevel >= :minLevel")
    List<PeerSkill> findBySkillAndMinLevel(
            @Param("skillName") String skillName,
            @Param("minLevel") Integer minLevel);
}
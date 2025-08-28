package com.corpclimb.repository;

import com.corpclimb.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeerRepository extends JpaRepository<Peer, Long> {
    @Query("SELECT p FROM Peer p WHERE p.department = :department")
    List<Peer> findByDepartment(@Param("department") String department);

    @Query("SELECT p FROM Peer p WHERE p.skillLevel >= :minSkill")
    List<Peer> findByMinSkillLevel(@Param("minSkill") Integer minSkill);
}
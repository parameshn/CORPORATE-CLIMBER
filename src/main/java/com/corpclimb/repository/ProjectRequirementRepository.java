package com.corpclimb.repository;

import com.corpclimb.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRequirementRepository extends JpaRepository<ProjectRequirement, Long> {
    @Query("SELECT pr FROM ProjectRequirement pr WHERE pr.project.id = :projectId")
    List<ProjectRequirement> findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT pr FROM ProjectRequirement pr WHERE pr.skillName = :skillName AND pr.requiredLevel <= :maxLevel")
    List<ProjectRequirement> findBySkillAndMaxLevel(
            @Param("skillName") String skillName,
            @Param("maxLevel") Integer maxLevel);
}
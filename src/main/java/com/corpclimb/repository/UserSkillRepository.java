package com.corpclimb.repository;

import com.corpclimb.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    @Query("SELECT us FROM UserSkill us WHERE us.user.id = :userId")
    List<UserSkill> findByUserId(@Param("userId") Long userId);

    @Query("SELECT us FROM UserSkill us WHERE us.user.id = :userId AND us.skillName = :skillName")
    List<UserSkill> findByUserIdAndSkillName(
            @Param("userId") Long userId,
            @Param("skillName") String skillName);

    @Query("SELECT us FROM UserSkill us WHERE us.currentLevel < us.targetLevel")
    List<UserSkill> findSkillsNeedingImprovement();
}

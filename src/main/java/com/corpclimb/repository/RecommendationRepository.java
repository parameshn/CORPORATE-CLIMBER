package com.corpclimb.repository;

import com.corpclimb.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    @Query("SELECT r FROM Recommendation r WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
    List<Recommendation> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("SELECT r FROM Recommendation r WHERE r.simulationRun.id = :simulationRunId")
    Recommendation findBySimulationRunId(@Param("simulationRunId") Long simulationRunId);

    @Query("SELECT r FROM Recommendation r WHERE r.recommendationType = 'PRIMARY'")
    List<Recommendation> findPrimaryRecommendations();
}
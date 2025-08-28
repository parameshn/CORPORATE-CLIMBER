package com.corpclimb.repository;

import com.corpclimb.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulationRunRepository extends JpaRepository<SimulationRun, Long> {
    @Query("SELECT sr FROM SimulationRun sr WHERE sr.user.id = :userId ORDER BY sr.simulatedAt DESC")
    List<SimulationRun> findByUserIdOrderBySimulatedAtDesc(@Param("userId") Long userId);

    @Query("SELECT sr FROM SimulationRun sr WHERE sr.user.id = :userId AND sr.project.id = :projectId ORDER BY sr.simulatedAt DESC")
    List<SimulationRun> findByUserIdAndProjectId(
            @Param("userId") Long userId,
            @Param("projectId") Long projectId);

    @Query("SELECT sr FROM SimulationRun sr WHERE sr.successProbability > :minProbability")
    List<SimulationRun> findHighSuccessSimulations(@Param("minProbability") Double minProbability);
}
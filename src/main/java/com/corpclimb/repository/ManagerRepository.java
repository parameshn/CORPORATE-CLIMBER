package com.corpclimb.repository;

import com.corpclimb.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    @Query("SELECT m FROM Manager m WHERE m.department = :department")
    List<Manager> findByDepartment(@Param("department") String department);

    @Query("SELECT m FROM Manager m WHERE m.leadershipScore >= :minScore")
    List<Manager> findByMinLeadershipScore(@Param("minScore") Double minScore);
}
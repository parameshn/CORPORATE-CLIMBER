package com.corpclimb.repository;

import com.corpclimb.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE p.domain = :domain")
    List<Project> findByDomain(@Param("domain") String domain);

    @Query("SELECT p FROM Project p WHERE p.manager.id = :managerId")
    List<Project> findByManagerId(@Param("managerId") Long managerId);

    @Query("SELECT p FROM Project p WHERE p.status = :status")
    List<Project> findByStatus(@Param("status") Project.ProjectStatus status);
}
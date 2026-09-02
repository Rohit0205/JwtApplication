package com.roh.jwtApplication.repository;

import com.roh.jwtApplication.entities.ApiPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApiPermissionRepository
        extends JpaRepository<ApiPermission, Long> {

    List<ApiPermission> findByHttpMethodAndStatus(
            String httpMethod,
            Character status
    );}

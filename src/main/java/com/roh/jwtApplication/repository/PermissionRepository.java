package com.roh.jwtApplication.repository;

import com.roh.jwtApplication.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission,Long> {

    Optional<Permission> findByName(String name);

}

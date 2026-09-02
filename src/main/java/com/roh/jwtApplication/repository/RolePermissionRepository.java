package com.roh.jwtApplication.repository;

import com.roh.jwtApplication.entities.BaseEntity;
import com.roh.jwtApplication.entities.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission,Long> {
    List<RolePermission> findByRoleId(Long roleId);

}

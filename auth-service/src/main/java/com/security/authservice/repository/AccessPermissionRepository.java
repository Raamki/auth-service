package com.security.authservice.repository;

import com.security.authservice.entity.AccessPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessPermissionRepository extends JpaRepository<AccessPermission, String> {
    AccessPermission findByNameIgnoreCase(String name);
}

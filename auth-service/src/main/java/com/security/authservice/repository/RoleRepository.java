package com.security.authservice.repository;

import com.security.authservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByNameIgnoreCaseAndIsActive(String name, Boolean isActive);
    Role findByName(String name);
}

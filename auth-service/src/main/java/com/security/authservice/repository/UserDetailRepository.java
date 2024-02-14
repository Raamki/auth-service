package com.security.authservice.repository;

import com.security.authservice.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, String> {
    UserDetail findTopByIdOrUsernameIgnoreCaseOrEmailIgnoreCase(String id, String username, String email);

    UserDetail findTopByUsernameIgnoreCase(String username);
    UserDetail findTopByIdOrUsernameIgnoreCase(String id, String username);

    @Modifying
    @Query("UPDATE UserDetail set isActive = false where role.id = :roleId")
    void deactivateUsers(@Param("roleId") String roleId);
}

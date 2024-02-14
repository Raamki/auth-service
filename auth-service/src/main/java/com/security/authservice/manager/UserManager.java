package com.security.authservice.manager;

import com.security.authservice.dto.request.PasswordRequest;
import com.security.authservice.dto.request.PermissionRequest;
import com.security.authservice.dto.request.RoleRequest;
import com.security.authservice.dto.request.UserRequest;
import com.security.authservice.dto.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserManager {
    UserResponse getUser(String username);
    UserResponse addUser(UserRequest userRequest);
    UserResponse addGuestUser();
    UserResponse updatePassword(PasswordRequest request);
    UserResponse modifyUserRole(UserRequest userRequest);
    UserResponse addRole(RoleRequest roleRequest);
    UserResponse modifyRolePermission(RoleRequest roleRequest);
    UserResponse addPermission(PermissionRequest permissionRequest);
    UserResponse modifyPermission(PermissionRequest permissionRequest);
    UserResponse activateUser(String username);
    UserResponse deActivateUser(String username);
    UserResponse activateRole(String role);
    UserResponse deActivateRole(String role);
}

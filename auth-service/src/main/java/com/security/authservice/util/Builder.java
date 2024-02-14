package com.security.authservice.util;

import com.security.authservice.dto.request.PermissionRequest;
import com.security.authservice.dto.request.RoleRequest;
import com.security.authservice.dto.request.UserRequest;
import com.security.authservice.dto.response.Permission;
import com.security.authservice.dto.response.Status;
import com.security.authservice.dto.response.UserResponse;
import com.security.authservice.entity.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.security.authservice.util.AuthUtil.getRandomUUID;
import static com.security.authservice.util.Constants.AUTO_CREATION;
import static com.security.authservice.util.Constants.USER_FETCH_SUCCESS;

public class Builder {
    public static UserDetail buildUserDetail(UserDetail userDetail, UserRequest request) {
        if (!Objects.nonNull(userDetail)) {
            userDetail = new UserDetail();
            userDetail.setId(getRandomUUID());
            userDetail.setCreatedBy(request.getCreatedBy());
            userDetail.setCreatedDateTime(new Date());
        }
        userDetail.setUsername(request.getUsername());
        userDetail.setPassword(request.getPassword());
        userDetail.setEmail(request.getEmail());
        userDetail.setModifiedBy(request.getCreatedBy());
        userDetail.setModifiedDateTime(new Date());
        userDetail.setIsActive(true);
        return userDetail;
    }

    public static Role buildRole(Role role, RoleRequest request) {
        if (!Objects.nonNull(role)) {
            role = new Role();
            role.setId(getRandomUUID());
            role.setCreatedBy(request.getCreatedBy());
            role.setCreatedDateTime(new Date());
        }
        role.setName(request.getRole().toUpperCase());
        role.setDescription(request.getRoleDescription());
        role.setModifiedBy(request.getCreatedBy());
        role.setModifiedDateTime(new Date());
        role.setIsActive(true);
        return role;
    }

    public static List<AccessPermission> buildAccessPermission(List<PermissionRequest> accessPermissions) {
        List<AccessPermission> accessPermissionList = new ArrayList<>();
        for (PermissionRequest request: accessPermissions){
            AccessPermission accessPermission = new AccessPermission();
            accessPermission.setId(getRandomUUID());
            accessPermission.setCreatedBy(request.getCreatedBy());
            accessPermission.setCreatedDateTime(new Date());
            accessPermission.setName(request.getPermission().toUpperCase());
            accessPermission.setDescription(request.getPermissionDescription());
            accessPermission.setModifiedBy(request.getCreatedBy());
            accessPermission.setModifiedDateTime(new Date());
            accessPermissionList.add(accessPermission);
        }
        return accessPermissionList;
    }

    public static UserResponse buildUserResponse(HttpStatus status, String message, String desc) {
        return UserResponse.builder()
                .status(Status.builder()
                        .code(status.value())
                        .message(message)
                        .build())
                .description(desc)
                .build();
    }

    public static UserResponse buildGetUserResponse(HttpStatus status, UserDetail userDetail) {
        return UserResponse.builder()
                .status(Status.builder()
                        .code(status.value())
                        .message(USER_FETCH_SUCCESS)
                        .build())
                .userId(userDetail.getId())
                .username(userDetail.getUsername())
                .email(userDetail.getEmail())
                .role(userDetail.getRole().getName())
                .permission(buildPermission(userDetail.getRole().getAccessPermissions()))
                .build();
    }

    private static List<Permission> buildPermission(List<AccessPermission> accessPermissions) {
        List<Permission> permissions = new ArrayList<>();
        for (AccessPermission permission: accessPermissions){
            permissions.add(Permission.builder()
                    .permission(permission.getName())
                    .permissionDescription(permission.getDescription())
                    .build()
            );
        }
        return permissions;
    }

    public static UserDetail buildGuestUserDetail(){
        UserDetail userDetail = new UserDetail();
        userDetail.setId(getRandomUUID());
        userDetail.setCreatedBy(AUTO_CREATION);
        userDetail.setCreatedDateTime(new Date());
        userDetail.setUsername(getRandomUUID());
        userDetail.setPassword(getRandomUUID());
        userDetail.setEmail(null);
        userDetail.setModifiedBy(AUTO_CREATION);
        userDetail.setModifiedDateTime(new Date());
        userDetail.setIsActive(true);
        return userDetail;
    }
}

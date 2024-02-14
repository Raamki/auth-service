package com.security.authservice.manager.impl;

import com.security.authservice.dto.request.PasswordRequest;
import com.security.authservice.dto.request.PermissionRequest;
import com.security.authservice.dto.request.RoleRequest;
import com.security.authservice.dto.request.UserRequest;
import com.security.authservice.dto.response.UserResponse;
import com.security.authservice.entity.AccessPermission;
import com.security.authservice.entity.Role;
import com.security.authservice.entity.UserDetail;
import com.security.authservice.jwt.JwtProvider;
import com.security.authservice.manager.UserManager;
import com.security.authservice.repository.AccessPermissionRepository;
import com.security.authservice.repository.RoleRepository;
import com.security.authservice.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.security.authservice.util.Builder.*;
import static com.security.authservice.util.Constants.*;

@Service
public class UserManagerImpl implements UserManager {
    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${user.expire}")
    private int userExpire;

    @Value("${guest.expire}")
    private int guestExpire;

    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccessPermissionRepository permissionRepository;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public UserResponse getUser(String username) {
        try {
            UserDetail userDetail = userDetailRepository.findTopByIdOrUsernameIgnoreCase(username, username);
            if (!Objects.nonNull(userDetail))
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, USER_UNAVAILABLE);
            if (!userDetail.getIsActive())
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, USER_INACTIVE);
            return buildGetUserResponse(HttpStatus.OK, userDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        try {
            UserDetail userDetail = userDetailRepository.findTopByIdOrUsernameIgnoreCaseOrEmailIgnoreCase(userRequest.getUserId(), userRequest.getUsername(), userRequest.getEmail());
            if (Objects.nonNull(userDetail)) {
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, USERNAME_EMAIL_AVAILABLE);
            }
            userDetail = buildUserDetail(null, userRequest);
            UserResponse userResponse =  setRoleAndSave(userDetail, userRequest);
            String jwtToken = jwtProvider.encryptUserUniqueId(userResponse, jwtSecretKey, userExpire);
            userResponse.setJwtToken(jwtToken);
            return userResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }

    @Override
    public UserResponse addGuestUser() {
        try {
            UserDetail userDetail = buildGuestUserDetail();
            userDetail.setRole(roleRepository.findByName(GUEST_ROLE));
            userDetailRepository.save(userDetail);
            UserResponse userResponse = buildGetUserResponse(HttpStatus.OK, userDetail);
            String jwtToken = jwtProvider.encryptUserUniqueId(userResponse, jwtSecretKey, guestExpire);
            userResponse.setJwtToken(jwtToken);
            return userResponse;
        } catch (Exception e){
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }

    private UserResponse setRoleAndSave(UserDetail userDetail, UserRequest userRequest) {
        try {
            Role role = roleRepository.findByNameIgnoreCaseAndIsActive(userRequest.getRole(), true);
            if (!Objects.nonNull(role)) {
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, ROLE_UNAVAILABLE);
            }
            userDetail.setRole(role);
            userDetailRepository.save(userDetail);
            return buildGetUserResponse(HttpStatus.OK, userDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }

    @Override
    public UserResponse updatePassword(PasswordRequest request) {
        try {
            UserDetail userDetail = userDetailRepository.findTopByUsernameIgnoreCase(request.getUsername());
            if (!Objects.nonNull(userDetail)) {
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, USER_UNAVAILABLE);
            }
            if (!userDetail.getPassword().equals(request.getOldPassword()))
                return buildUserResponse(HttpStatus.UNAUTHORIZED, UNAUTHORISED_MSG, INCORRECT_PASSWORD);
            userDetail.setPassword(request.getNewPassword());
            userDetailRepository.save(userDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
        return buildUserResponse(HttpStatus.OK, OK_REQUEST, PASSWORD_UPDATE_SUCCESS);
    }

    @Override
    public UserResponse modifyUserRole(UserRequest userRequest) {
        try {
            UserDetail userDetail = userDetailRepository.findTopByUsernameIgnoreCase(userRequest.getUsername());
            if (!Objects.nonNull(userDetail)) {
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, USER_UNAVAILABLE);
            }
            setRoleAndSave(userDetail, userRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
        return buildUserResponse(HttpStatus.OK, OK_REQUEST, USER_MODIFICATION_SUCCESS);
    }

    @Override
    public UserResponse addRole(RoleRequest roleRequest) {
        try {
            Role role = roleRepository.findByName(roleRequest.getRole());
            if (Objects.nonNull(role)) {
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, ROLE_AVAILABLE);
            }
            role = buildRole(null, roleRequest);
            return setPermissionsAndSave(role, roleRequest.getPermissionRequestList());
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }

    private UserResponse setPermissionsAndSave(Role role, List<String> permissionRequestList) {
        try {
            List<AccessPermission> permissions = new ArrayList<>();
            for (String permissionName : permissionRequestList) {
                AccessPermission permission = permissionRepository.findByNameIgnoreCase(permissionName);
                if (!Objects.nonNull(permission)) {
                    return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, ROLE_PERMISSION_UNAVAILABLE);
                }
                permissions.add(permission);
            }
            role.setAccessPermissions(permissions);
            roleRepository.save(role);
            return buildUserResponse(HttpStatus.OK, OK_REQUEST, ROLE_MODIFICATION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }

    @Override
    public UserResponse modifyRolePermission(RoleRequest roleRequest) {
        try {
            Role role = roleRepository.findByName(roleRequest.getRole());
            if (!Objects.nonNull(role)) {
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, ROLE_UNAVAILABLE);
            }
            return setPermissionsAndSave(role, roleRequest.getPermissionRequestList());
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }

    @Override
    public UserResponse addPermission(PermissionRequest request) {
        try {
            AccessPermission permission = permissionRepository.findByNameIgnoreCase(request.getPermission());
            if (Objects.nonNull(permission)) {
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, ROLE_PERMISSION_AVAILABLE);
            }
            permission = buildAccessPermission(Collections.singletonList(request)).get(0);
            permissionRepository.save(permission);
            return buildUserResponse(HttpStatus.OK, OK_REQUEST, ROLE_PERMISSION_CREATION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }

    @Override
    public UserResponse modifyPermission(PermissionRequest request) {
        try {
            AccessPermission permission = permissionRepository.findByNameIgnoreCase(request.getPermission());
            if (!Objects.nonNull(permission)) {
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, ROLE_PERMISSION_UNAVAILABLE);
            }
            permission = buildAccessPermission(Collections.singletonList(request)).get(0);
            permissionRepository.save(permission);
            return buildUserResponse(HttpStatus.OK, OK_REQUEST, ROLE_PERMISSION_MODIFICATION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }

    @Override
    public UserResponse activateUser(String username) {
        try {
            UserDetail userDetail = userDetailRepository.findTopByUsernameIgnoreCase(username);
            if (!Objects.nonNull(userDetail)) {
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, USER_UNAVAILABLE);
            }
            userDetail.setIsActive(true);
            userDetailRepository.save(userDetail);
            return buildUserResponse(HttpStatus.OK, OK_REQUEST, USER_ACTIVATED);
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }

    @Override
    public UserResponse deActivateUser(String username) {
        try {
            UserDetail userDetail = userDetailRepository.findTopByUsernameIgnoreCase(username);
            if (!Objects.nonNull(userDetail)) {
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, USER_UNAVAILABLE);
            }
            userDetail.setIsActive(false);
            userDetailRepository.save(userDetail);
            return buildUserResponse(HttpStatus.OK, OK_REQUEST, USER_DEACTIVATED);
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }

    @Override
    public UserResponse activateRole(String roleName) {
        try {
            Role role = roleRepository.findByName(roleName);
            if (!Objects.nonNull(role)) {
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, ROLE_UNAVAILABLE);
            }
            role.setIsActive(true);
            roleRepository.save(role);
            return buildUserResponse(HttpStatus.OK, OK_REQUEST, ROLE_ACTIVATED);
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }

    @Override
    public UserResponse deActivateRole(String roleName) {
        try {
            Role role = roleRepository.findByName(roleName);
            if (!Objects.nonNull(role)) {
                return buildUserResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, ROLE_UNAVAILABLE);
            }
            role.setIsActive(false);
            userDetailRepository.deactivateUsers(role.getId());
            roleRepository.save(role);
            return buildUserResponse(HttpStatus.OK, OK_REQUEST, ROLE_DEACTIVATED);
        } catch (Exception e) {
            e.printStackTrace();
            return buildUserResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, UNABLE_TO_PROCESS);
        }
    }
}

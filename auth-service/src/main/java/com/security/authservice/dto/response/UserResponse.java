package com.security.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder
public class UserResponse {
    private Status status;
    private String description;
    private String userId;
    private String username;
    private String email;
    private String role;
    private String roleDescription;
    private String jwtToken;
    private List<Permission> permission;
}

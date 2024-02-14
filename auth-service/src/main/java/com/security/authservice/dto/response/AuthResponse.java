package com.security.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@Builder
public class AuthResponse {
    private Status status;
    private String authToken;
    private String userId;
    private String username;
    private Boolean isValidUser;
    private String role;
}

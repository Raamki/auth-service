package com.security.authservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class PasswordRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}

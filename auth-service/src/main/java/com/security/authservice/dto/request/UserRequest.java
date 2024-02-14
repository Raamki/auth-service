package com.security.authservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserRequest {
    private String userId;
    @NotNull(message = "username is mandatory and must not be null")
    @NotBlank(message = "username is mandatory and must not be Empty")
    private String username;
    @NotNull(message = "password is mandatory and must not be null")
    @NotBlank(message = "password is mandatory and must not be Empty")
    private String password;
    private String email;
    @NotNull(message = "createdBy is mandatory and must not be null")
    @NotBlank(message = "createdBy is mandatory and must not be Empty")
    private String createdBy;
    @NotNull(message = "role is mandatory and must not be null")
    @NotBlank(message = "role is mandatory and must not be Empty")
    private String role;
}

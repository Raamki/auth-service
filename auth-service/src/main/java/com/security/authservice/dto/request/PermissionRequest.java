package com.security.authservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PermissionRequest {
    @NotNull(message = "permission is mandatory and must not be null")
    @NotBlank(message = "permission is mandatory and must not be Empty")
    private String permission;
    @NotNull(message = "permission description is mandatory and must not be null")
    @NotBlank(message = "permission description is mandatory and must not be Empty")
    private String permissionDescription;
    @NotNull(message = "createdBy is mandatory and must not be null")
    @NotBlank(message = "createdBy is mandatory and must not be Empty")
    private String createdBy;
}

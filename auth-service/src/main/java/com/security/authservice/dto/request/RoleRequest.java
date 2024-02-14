package com.security.authservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoleRequest {
    @NotNull(message = "role is mandatory and must not be null")
    @NotBlank(message = "role is mandatory and must not be Empty")
    private String role;
    @NotNull(message = "role description is mandatory and must not be null")
    @NotBlank(message = "role description is mandatory and must not be Empty")
    private String roleDescription;
    @NotNull(message = "permission is mandatory and must not be null")
    @NotBlank(message = "permission is mandatory and must not be Empty")
    private List<String> permissionRequestList;
    @NotNull(message = "createdBy is mandatory and must not be null")
    @NotBlank(message = "createdBy is mandatory and must not be Empty")
    private String createdBy;
}

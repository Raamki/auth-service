package com.security.authservice.exception;

import com.security.authservice.dto.response.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerificationException extends Exception {
    private Status status;
    private String description;
}

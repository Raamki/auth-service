package com.security.authservice.util;

import com.security.authservice.dto.response.AuthResponse;
import com.security.authservice.dto.response.Status;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.security.authservice.util.Constants.AUTH_SUCCESS;

public class AuthUtil {
    public static AuthResponse buildAuthResponse(HttpStatus status, String message, String jwt){
        return AuthResponse.builder()
                .status(
                        Status.builder()
                                .code(status.value())
                                .message(message)
                                .build()
                )
                .authToken(jwt)
                .build();
    }

    public static AuthResponse buildUserSuccessAuthResponse(String userId, String username, String role){
        return AuthResponse.builder()
                .status(
                        Status.builder()
                                .code(HttpStatus.OK.value())
                                .message(AUTH_SUCCESS)
                                .build()
                )
                .userId(userId)
                .username(username)
                .role(role)
                .isValidUser(true)
                .build();
    }

    public static AuthResponse buildUserFailureAuthResponse(String message){
        return AuthResponse.builder()
                .status(
                        Status.builder()
                                .code(HttpStatus.OK.value())
                                .message(message)
                                .build()
                )
                .isValidUser(false)
                .build();
    }

    public static String getRandomUUID(){
        return UUID.randomUUID().toString();
    }
}

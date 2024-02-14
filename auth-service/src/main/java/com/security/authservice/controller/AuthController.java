package com.security.authservice.controller;

import com.security.authservice.dto.request.AuthRequest;
import com.security.authservice.dto.response.AuthResponse;
import com.security.authservice.dto.response.UserResponse;
import com.security.authservice.exception.VerificationException;
import com.security.authservice.jwt.JwtProvider;
import com.security.authservice.manager.UserManager;
import com.security.authservice.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Objects;

import static com.security.authservice.util.AuthUtil.buildAuthResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${user.expire}")
    private int userExpire;

    @Value("${guest.expire}")
    private int guestExpire;

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody @Valid AuthRequest authRequest) throws VerificationException {
        if (authRequest.getUserId() == null || authRequest.getUserId().length() == 0) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            } catch (Exception e) {
                return new ResponseEntity<>(buildAuthResponse(HttpStatus.UNAUTHORIZED,
                        Constants.INVALID_CREDENTIALS, null), HttpStatus.UNAUTHORIZED);
            }
        }
        final UserResponse userResponse = userManager.getUser(
                Objects.nonNull(authRequest.getUserId()) ? authRequest.getUserId() : authRequest.getUsername());
        if (userResponse.getStatus().getCode() != HttpStatus.OK.value())
            return new ResponseEntity(userResponse, HttpStatus.valueOf(userResponse.getStatus().getCode()));
        String jwtToken;
        if (userResponse.getRole().equalsIgnoreCase("GUEST"))
            jwtToken = jwtProvider.encryptUserUniqueId(userResponse, jwtSecretKey, guestExpire);
        else
            jwtToken = jwtProvider.encryptUserUniqueId(userResponse, jwtSecretKey, userExpire);
        return new ResponseEntity(buildAuthResponse(HttpStatus.OK, Constants.AUTH_SUCCESS, jwtToken), HttpStatus.OK);
    }

    @GetMapping("/validate-token")
    public ResponseEntity validateToken(@RequestParam String token) {
        AuthResponse authResponse = jwtProvider.decryptToken(token, jwtSecretKey);
        if (authResponse.getIsValidUser()) {
            return new ResponseEntity(authResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(authResponse, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity refreshToken(@RequestParam String token) {
        AuthResponse authResponse = jwtProvider.decryptToken(token, jwtSecretKey);
        if (authResponse.getIsValidUser()) {
            final UserResponse userResponse = userManager.getUser(authResponse.getUsername());
            String jwtToken;
            if (userResponse.getRole().equalsIgnoreCase("GUEST"))
                jwtToken = jwtProvider.encryptUserUniqueId(userResponse, jwtSecretKey, guestExpire);
            else
                jwtToken = jwtProvider.encryptUserUniqueId(userResponse, jwtSecretKey, userExpire);
            return new ResponseEntity(buildAuthResponse(HttpStatus.OK, Constants.TOKEN_REFRESH_SUCCESS, jwtToken), HttpStatus.OK);
        }
        return new ResponseEntity<>(authResponse, HttpStatus.UNAUTHORIZED);
    }
}

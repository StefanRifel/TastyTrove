package com.tastytrove.service;

import com.tastytrove.payload.request.JwtRefreshRequest;
import com.tastytrove.payload.request.LoginRequest;
import com.tastytrove.payload.request.RegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> signUp(RegistrationRequest registrationRequest);
    ResponseEntity<?> signIn(LoginRequest loginRequest);
    ResponseEntity<?> refreshToken(JwtRefreshRequest refreshRequest);
}

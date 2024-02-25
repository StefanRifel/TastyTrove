package com.tastytrove.service;

import com.tastytrove.payload.request.LoginRequest;
import com.tastytrove.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> signup(SignupRequest request);
    ResponseEntity<?> login(LoginRequest request);
}

package com.tastytrove.controller;

import com.tastytrove.payload.request.JwtRefreshRequest;
import com.tastytrove.payload.request.LoginRequest;
import com.tastytrove.payload.request.RegistrationRequest;
import com.tastytrove.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest){
        return authenticationService.signUp(registrationRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        return authenticationService.signIn(loginRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody JwtRefreshRequest refreshRequest){
        return authenticationService.refreshToken(refreshRequest);
    }
}

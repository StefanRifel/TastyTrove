package com.tastytrove.service;

import com.tastytrove.jwt.auth.AuthenticationRequest;
import com.tastytrove.jwt.auth.AuthenticationResponse;
import com.tastytrove.jwt.auth.RegisterRequest;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}

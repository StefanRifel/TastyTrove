package com.tastytrove.serviceimpl;

import com.tastytrove.jwt.auth.AuthenticationRequest;
import com.tastytrove.jwt.auth.AuthenticationResponse;
import com.tastytrove.jwt.auth.RegisterRequest;
import com.tastytrove.service.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        return null;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return null;
    }
}

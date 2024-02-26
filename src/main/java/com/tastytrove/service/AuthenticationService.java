package com.tastytrove.service;

import com.tastytrove.payload.ReqRes;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ReqRes signUp(ReqRes request);
    ReqRes signIn(ReqRes request);
    ReqRes refreshToken(ReqRes request);
}

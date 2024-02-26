package com.tastytrove.serviceimpl;

import com.tastytrove.entity.Role;
import com.tastytrove.entity.User;
import com.tastytrove.jwt.JwtUtils;
import com.tastytrove.payload.ReqRes;
import com.tastytrove.repository.UserRepository;
import com.tastytrove.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;

    @Override
    public ReqRes signUp(ReqRes signupRequest){
        ReqRes response = new ReqRes();
        try {
            User user = User.builder()
                    .email(signupRequest.getEmail())
                    .password(passwordEncoder.encode(signupRequest.getPassword()))
                    .role(new Role(signupRequest.getRole()))
                    .build();

            User userResult = userRepository.save(user);
            if(userResult != null && userResult.getId() > 0) {
                //response.setOurUsers(userResult);
                response.setMessage("User Saved Successfully");
                response.setStatusCode(200);
            }

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }

        return response;
    }

    @Override
    public ReqRes signIn(ReqRes loginRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            log.info("User is: {}", user);
            String jwt = jwtUtils.generateJwtToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    @Override
    public ReqRes refreshToken(ReqRes request) {
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(request.getToken());
        User users = userRepository.findByEmail(ourEmail).orElseThrow();
        if (jwtUtils.isValid(request.getToken(), users)) {
            var jwt = jwtUtils.generateJwtToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(request.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        } else {
            response.setStatusCode(500);
            response.setError("Error refresh Token");
        }

        return response;
    }
}

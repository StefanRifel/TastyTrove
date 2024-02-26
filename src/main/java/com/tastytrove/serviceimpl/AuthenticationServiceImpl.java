package com.tastytrove.serviceimpl;

import com.tastytrove.entity.Role;
import com.tastytrove.entity.User;
import com.tastytrove.jwt.JwtUtils;
import com.tastytrove.payload.request.JwtRefreshRequest;
import com.tastytrove.payload.request.LoginRequest;
import com.tastytrove.payload.request.RegistrationRequest;
import com.tastytrove.payload.response.JwtResponse;
import com.tastytrove.payload.response.MessageResponse;
import com.tastytrove.repository.RoleRepository;
import com.tastytrove.repository.UserRepository;
import com.tastytrove.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> signUp(RegistrationRequest registrationRequest){
        MessageResponse response = new MessageResponse();

        //Check if user already exist with given signupRequest
        //todo extract messages in own methode for different cases
        if(userRepository.existsByEmail(registrationRequest.getEmail())){
            response.setMessage("Email is already in use!");
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
            return ResponseEntity.badRequest().body(response);
        } else if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            response.setMessage("Username is already taken!");
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
            return ResponseEntity.badRequest().body(response);
        }

        List<String> requestRoles = registrationRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        for (String role: requestRoles){
            Optional<Role> insertedRole = roleRepository.findByName(role);
            if(insertedRole.isPresent()) {
                roles.add(insertedRole.get());
            } else {
                response.setMessage("role not found: " + role);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
                return ResponseEntity.badRequest().body(response);
            }
        }

        try {
            User user = User.builder()
                    .username(registrationRequest.getUsername())
                    .firstname(registrationRequest.getFirstname())
                    .lastname(registrationRequest.getLastname())
                    .email(registrationRequest.getEmail())
                    .password(passwordEncoder.encode(registrationRequest.getPassword()))
                    .roles(roles)
                    .build();

            userRepository.save(user);

            response.setMessage("user successfully registered");
            response.setStatusCode(HttpStatus.OK.value());

        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> signIn(LoginRequest loginRequest) {
        JwtResponse response = new JwtResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword()));

            Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
            if(user.isEmpty()) {
                response.setMessage("user not found with email: " + loginRequest.getEmail());
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
                return ResponseEntity.badRequest().body(response);
            }

            String jwt = jwtUtils.generateJwtToken(user.get());
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user.get());

            response.setStatusCode(HttpStatus.OK.value());
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> refreshToken(JwtRefreshRequest refreshRequest) {
        JwtResponse response = new JwtResponse();
        String email = jwtUtils.extractUsername(refreshRequest.getToken());
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            response.setMessage("user not found with email: " + email);
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
            return ResponseEntity.badRequest().body(response);
        }


        if (jwtUtils.isValid(refreshRequest.getToken(), user.get())) {
            var jwt = jwtUtils.generateJwtToken(user.get());

            response.setStatusCode(HttpStatus.OK.value());
            response.setToken(jwt);
            response.setRefreshToken(refreshRequest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("successfully refreshed token");
        } else {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("token is not valid for user: " + email);
            response.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }

        return ResponseEntity.ok(response);
    }
}

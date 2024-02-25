package com.tastytrove.serviceimpl;

import com.tastytrove.entity.Role;
import com.tastytrove.entity.User;
import com.tastytrove.entity.UserRole;
import com.tastytrove.jwt.JwtUtils;
import com.tastytrove.payload.request.LoginRequest;
import com.tastytrove.payload.request.SignupRequest;
import com.tastytrove.payload.response.JwtResponse;
import com.tastytrove.payload.response.MessageResponse;
import com.tastytrove.repository.RoleRepository;
import com.tastytrove.repository.UserRepository;
import com.tastytrove.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public ResponseEntity<?> signup(SignupRequest signupRequest){

        //Check if user already exist with given signupRequest
        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        } else if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        User user = User.builder()
                .firstname(signupRequest.getFirstname())
                .lastname(signupRequest.getLastname())
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();

        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByUserRole(UserRole.USER);
        if(userRole.isPresent()){
            roles.add(userRole.get());
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("\"Error: Role is not found.\""));
        }

        user.setRoles(roles);
        userRepository.save(user);
        String token = jwtUtils.generateJwtToken(user);
        return ResponseEntity.ok(new MessageResponse(token));
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
        String token = token = jwtUtils.generateJwtToken(user);


        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtResponse jwtResponse = JwtResponse.builder()
                .token(token)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(UserRole.USER.toString())
                .build();


        return ResponseEntity.ok(jwtResponse);
    }
}

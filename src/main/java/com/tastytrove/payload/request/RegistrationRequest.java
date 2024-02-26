package com.tastytrove.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private List<String> roles;
}

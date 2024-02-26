package com.tastytrove.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private int statusCode;
    private String error;
    private String message;
    private String type = "Bearer";
    private String token;
    private String refreshToken;
    private String expirationTime;
}

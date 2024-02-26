package com.tastytrove.jwt;

import io.jsonwebtoken.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;


@Component
public class JwtUtils {

    private static final String SECRET_KEY = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
    private  static  final long EXPIRATION_TIME = 86400000;

    private SecretKey getSignInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateJwtToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignInKey())
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignInKey())
                .compact();
    }
}

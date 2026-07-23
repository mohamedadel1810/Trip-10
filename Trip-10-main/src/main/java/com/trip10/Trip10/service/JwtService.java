package com.trip10.Trip10.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {


    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secretKey)
        );
    }

    public String generateToken(String email, String role) {

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()
                        + 1000 * 60 * 60 * 24)) // 24 hours
                .signWith(getSigningKey())
                .compact();
    }


    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }


    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }


    public boolean isTokenValid(String token, String email) {
        return extractUsername(token).equals(email)
                && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }


    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

package com.programmingtechie.userservice.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    // Replace with an environment variable or a more secure method for storing the secret key in production
    private String jwtSecret = "snAUf/UWhgqvtPUoGMsUne/eL6xXtFH6OwU9JOiLkVo=";
    private long jwtExpirationDate = 3600000; // 1 hour = 3600 seconds * 1000 = 3600000 milliseconds

    // Use a method to retrieve the secret key
//    private SecretKey getSecretKey() {
//        return new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
//    }
// In JwtUtil.java
    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);  // Use the same BASE64 decoding
        return Keys.hmacShaKeyFor(keyBytes);  // Use Keys.hmacShaKeyFor() method for consistency
    }

    // Generates a JWT token for a user
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationDate)) // Use jwtExpirationDate here
                .signWith(getSecretKey()) // Use the method to get the secret key
                .compact();
    }

    // Extracts claims from the JWT token
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSecretKey()) // Use the method to get the secret key
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e); // Use a standard exception
        }
    }


    // Extracts the username from the token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extracts roles from the token
    public List<String> extractRoles(String token) {
        return (List<String>) extractClaims(token).get("roles");
    }

    // Checks if the token has expired
    public boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration != null && expiration.before(new Date());
    }

    // Validates the token by checking the username and expiration
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}

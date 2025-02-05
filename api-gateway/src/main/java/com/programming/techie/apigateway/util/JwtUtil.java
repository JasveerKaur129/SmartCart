package com.programming.techie.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;


@Component
public class JwtUtil {

    private static final String SECRET_KEY = "snAUf/UWhgqvtPUoGMsUne/eL6xXtFH6OwU9JOiLkVo=";
    private long jwtExpirationDate = 3600000; //1h = 3600s and 3600*1000 = 3600000 milliseconds

//    public String generateToken(String username, String roles) {
//
//        Date currentDate = new Date();
//        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
//
//        String token = Jwts.builder()
//                .subject(username)
//                .claim("roles", roles)
//                .issuedAt(new Date())
//                .expiration(expireDate)
//                .signWith(key(), SignatureAlgorithm.HS256)
//                .compact();
//
//        return token;
//    }

    public Key getSecretKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractClaims(token);
        return (List<String>) claims.get("roles", List.class);  // Assumes roles are stored as a list
    }


    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}


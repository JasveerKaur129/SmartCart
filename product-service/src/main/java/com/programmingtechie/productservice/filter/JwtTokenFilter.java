package com.programmingtechie.productservice.filter;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


import javax.crypto.SecretKey;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String SECRET_KEY = "snAUf/UWhgqvtPUoGMsUne/eL6xXtFH6OwU9JOiLkVo=";
    public Key getSecretKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, javax.servlet.http.HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = extractTokenFromHeader(request);
        if (token != null && validateToken(token)) {
            Claims claims = parseToken(token);
            String username = claims.getSubject();
            List<String> roles = (List<String>) claims.get("roles");

            // Create the authentication object with roles and set it to the SecurityContext
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username, null, roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        chain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);  // Remove "Bearer " from the token
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            // Parse the token and validate the signature
            Jwts.parser()
                    .setSigningKey(getSecretKey())  // Set the key for signature verification
                    .build()
                    .parseClaimsJws(token);  // This will throw an exception if the token is invalid
            return true;
        } catch (io.jsonwebtoken.SignatureException e) {
            System.out.println("Invalid JWT signature");
            return false;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("JWT token has expired");
            return false;
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported");
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("JWT token is illegal or empty");
            return false;
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

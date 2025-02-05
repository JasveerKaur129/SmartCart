package com.programming.techie.apigateway.filter;


import com.programming.techie.apigateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Extract JWT token from Authorization header
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        System.out.println("token"+token);
        // If token is not present or malformed, return 401 Unauthorized
        if (token == null || !token.startsWith("Bearer ")) {
            return unauthorizedResponse(exchange);
        }

        token = token.substring(7); // Remove "Bearer " prefix
        System.out.println("tUtil.isTokenExpired(token) && jwtUtil.extractUsername(token)"+jwtUtil.isTokenExpired(token)+ jwtUtil.extractUsername(token));
        // Validate the token
        if (!jwtUtil.isTokenExpired(token) && jwtUtil.extractUsername(token) != null) {
            // If valid, add user info to the request headers
            exchange = addUserInfoToRequest(exchange, token);
            return chain.filter(exchange); // Forward the request to the downstream service
        }

        return unauthorizedResponse(exchange); // If token is invalid, return 401 Unauthorized
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private ServerWebExchange addUserInfoToRequest(ServerWebExchange exchange, String token) {
        // Add user info (username, roles) to request headers
        String username = jwtUtil.extractUsername(token);
        String roles= jwtUtil.extractRoles(token).stream()
                .map(String::toUpperCase)
                .collect(Collectors.joining(","));

        System.out.println("roles"+roles);
        return exchange.mutate()
                .request(request -> request
                        .header("X-Username", username) // Add username to headers
                        .header("X-Roles", roles)       // Add roles to headers
                ).build();
    }
}

package com.programming.techie.apigateway.config;

import com.programming.techie.apigateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/product/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))  // Apply JwtAuthenticationFilter to the route
                        .uri("lb://product-service"))
                .route(r -> r.path("/api/order/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))  // Apply JwtAuthenticationFilter to the route
                        .uri("lb://order-service"))
                .route(r -> r.path("/api/user/**")// Apply JwtAuthenticationFilter to the route
                        .uri("lb://user-service"))
                .build();
    }
}

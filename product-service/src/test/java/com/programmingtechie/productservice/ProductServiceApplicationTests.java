package com.programmingtechie.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ProductService productService;
//
//    @BeforeEach
//    void setUp() {
//        // Mock the void method createProduct to do nothing
//        doNothing().when(productService).createProduct(any(ProductRequest.class));
//
//        // Create a Collection of authorities with "ADMIN" role
//        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
//        authorities.add(new SimpleGrantedAuthority("ADMIN"));

        // Mock the Authentication object
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getName()).thenReturn("admin");
//        when(authentication.getAuthorities()).thenReturn(authorities);

//        // Set the SecurityContext with the mock Authentication
//        SecurityContext securityContext = mock(SecurityContext.class);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);  // Set the context to the SecurityContextHolder
//    }
//
//    @Test
//    void shouldCreateProduct() throws Exception {
//        ProductRequest productRequest = getProductRequest();
//        String productRequestString = objectMapper.writeValueAsString(productRequest);
//
//        // Simulate an Authorization header with a Bearer token (mock token)
//        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/product")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer valid.jwt.token") // Mock the JWT token
//                        .content(productRequestString))
//                .andExpect(status().isCreated());  // Expecting a 201 Created status
//    }
//
//    private ProductRequest getProductRequest() {
//        return ProductRequest.builder()
//                .name("iPhone 13")
//                .description("iPhone 13")
//                .price(BigDecimal.valueOf(1200))
//                .skuCode("1234")
//                .quantity(1)
//                .build();
//    }
}

package com.programmingtechie.productservice.controller;

import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Endpoint to create a new product and update inventory
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            productService.createProduct(productRequest);  // Create product and update inventory
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Product created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating product: " + e.getMessage());
        }
    }

    // Endpoint to update an existing product and inventory (if required)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable int productId, @RequestBody ProductRequest productRequest) {
        try {
            productService.updateProduct(productId, productRequest);  // Update product and inventory
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Product updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating product: " + e.getMessage());
        }
    }

    // Endpoint to get all products
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        System.out.println("flag"+true);
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Endpoint to get a specific product by ID
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int productId) {
        try {
            ProductResponse productResponse = productService.getProductById(productId);
            return ResponseEntity.ok(productResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }
}

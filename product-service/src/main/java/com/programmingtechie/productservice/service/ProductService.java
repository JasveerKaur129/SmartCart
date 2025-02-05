package com.programmingtechie.productservice.service;

import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    private final InventoryServiceClient inventoryServiceClient;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .skuCode(productRequest.getSkuCode())
                .quantity(productRequest.getQuantity())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());

        inventoryServiceClient.updateInventoryQuantity(productRequest.getSkuCode(), productRequest.getQuantity());  // Add or update quantity in the inventory

    }

    public void updateProduct(int productId, ProductRequest productRequest) {
        // Check if the product exists
        Optional<Product> existingProductOpt = productRepository.findById(productId);

        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();

            // Update product details
            existingProduct.setName(productRequest.getName());
            existingProduct.setDescription(productRequest.getDescription());
            existingProduct.setPrice(productRequest.getPrice());
            existingProduct.setSkuCode(productRequest.getSkuCode());

            // Save the updated product
            productRepository.save(existingProduct);
            log.info("Product {} is updated", existingProduct.getId());

            // Check if inventory needs to be updated (add or subtract)
            if (existingProduct.getSkuCode().equals(productRequest.getSkuCode())) {
                // Update inventory quantity in the Inventory Service
                inventoryServiceClient.updateInventoryQuantity(productRequest.getSkuCode(), productRequest.getQuantity());
            }

        } else {
            throw new RuntimeException("Product not found with id: " + productId);
        }
    }

    public ProductResponse getProductById(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        return mapToProductResponse(product);
    }
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}

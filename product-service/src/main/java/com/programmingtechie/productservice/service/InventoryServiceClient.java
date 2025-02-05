package com.programmingtechie.productservice.service;

import com.programmingtechie.productservice.dto.InventoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@Component
@RequiredArgsConstructor
public class InventoryServiceClient {

    private final WebClient.Builder webClientBuilder;
    private static final String INVENTORY_SERVICE_URL = "http://inventory-service/api/inventory/update";


    // Method to call the update inventory endpoint of the inventory service
    public void updateInventoryQuantity(String skuCode, int quantity) {
        try {
            webClientBuilder.build().post()
                    .uri(INVENTORY_SERVICE_URL)
                    .bodyValue(new InventoryRequest(skuCode, quantity))  // Sending the SKU and quantity in the body
                    .retrieve()
                    .bodyToMono(Void.class)  // Expecting no response body, just a success code
                    .block();  // Block to wait for completion (could be replaced with async calls for better efficiency)
        } catch (WebClientResponseException e) {
            // Handle the case where the inventory service is unavailable or responds with an error
            throw new RuntimeException("Error calling Inventory Service: " + e.getMessage(), e);
        }
    }
}
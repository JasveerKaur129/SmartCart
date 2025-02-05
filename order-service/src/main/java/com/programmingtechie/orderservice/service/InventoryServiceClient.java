package com.programmingtechie.orderservice.service;

import com.programmingtechie.orderservice.dto.InventoryRequest;
import com.programmingtechie.orderservice.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryServiceClient {

    private final WebClient.Builder webClientBuilder;

    private static final String INVENTORY_SERVICE_URL = "http://inventory-service/api/inventory";

    // Method to check inventory for a list of SKU codes
    public InventoryResponse[] checkInventory(List<String> skuCodes) {
        return webClientBuilder.build()
                .get()
                .uri(INVENTORY_SERVICE_URL,
                        uriBuilder ->  uriBuilder.queryParam("skuCode", skuCodes).build())  // Join SKU codes to send as a query parameter
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();  // Block to wait for the response
    }

    // Reduce inventory after an order is placed
    public void reduceInventory(String skuCode, int quantity) {
        try {
            InventoryRequest inventoryRequest = new InventoryRequest(skuCode, quantity);

            webClientBuilder.build()
                    .put()
                    .uri(INVENTORY_SERVICE_URL + "/reduce/{skuCode}", skuCode)
                    .bodyValue(inventoryRequest)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException e) {
            // Handle error (log, rethrow, etc.)
            throw new IllegalArgumentException("Error reducing inventory for SKU: " + skuCode, e);
        }
    }
}

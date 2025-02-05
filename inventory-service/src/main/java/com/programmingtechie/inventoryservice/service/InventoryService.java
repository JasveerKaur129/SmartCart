package com.programmingtechie.inventoryservice.service;

import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.model.Inventory;
import com.programmingtechie.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    // Check if products are in stock (already present)
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        List<InventoryResponse> inventoryResponses = inventoryRepository.findBySkuCodeIn(skuCodes)
                .stream()
                .map(inventory -> new InventoryResponse(inventory.getSkuCode(), inventory.getQuantity() > 0))
                .collect(Collectors.toList());

        // If any SKU code is not in the inventory, return it with isInStock=false
        skuCodes.forEach(skuCode -> {
            if (inventoryResponses.stream().noneMatch(resp -> resp.getSkuCode().equals(skuCode))) {
                inventoryResponses.add(new InventoryResponse(skuCode, false));
            }
        });

        return inventoryResponses;
    }


    // Reduce inventory when an order is placed (already present)
    @Transactional
    public void reduceInventory(String skuCode, int quantity) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode);
        if (inventory != null && inventory.getQuantity() >= quantity) {
            inventory.setQuantity(inventory.getQuantity() - quantity);
            inventoryRepository.save(inventory);
        }
    }

    // Add inventory when a new product is created (new method)
    @Transactional
    public void updateInventoryQuantity(String skuCode, int quantity) {
        Inventory existingInventory = inventoryRepository.findBySkuCode(skuCode);

        if (existingInventory != null) {
            // Product already exists in the inventory, so update the quantity
            existingInventory.setQuantity(existingInventory.getQuantity() + quantity);
            inventoryRepository.save(existingInventory);  // Save updated inventory
        } else {
            // Product doesn't exist in inventory, so add it
            Inventory newInventory = new Inventory();
            newInventory.setSkuCode(skuCode);
            newInventory.setQuantity(quantity);
            inventoryRepository.save(newInventory);  // Save new inventory entry
        }
    }
}

package com.programmingtechie.inventoryservice.controller;

import com.programmingtechie.inventoryservice.dto.InventoryRequest;
import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.model.Inventory;
import com.programmingtechie.inventoryservice.repository.InventoryRepository;
import com.programmingtechie.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // Endpoint to check if products are in stock based on the SKU codes
    @GetMapping
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    // Endpoint to reduce inventory when an order is placed
    @PutMapping("/reduce/{skuCode}")
    public ResponseEntity<Void> reduceInventory(@PathVariable String skuCode, @RequestBody InventoryRequest inventoryRequest) {
        inventoryService.reduceInventory(skuCode, inventoryRequest.getQuantity());
        return ResponseEntity.ok().build();
    }

    // Endpoint to add or update inventory when a new product is created or quantity is updated
    @PostMapping("/update")
    public ResponseEntity<Void> updateInventory(@RequestBody InventoryRequest inventoryRequest) {
        inventoryService.updateInventoryQuantity(inventoryRequest.getSkuCode(), inventoryRequest.getQuantity());
        return ResponseEntity.ok().build();
    }
}
package com.programmingtechie.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class InventoryRequest {
    private String skuCode;
    private int quantity;
}

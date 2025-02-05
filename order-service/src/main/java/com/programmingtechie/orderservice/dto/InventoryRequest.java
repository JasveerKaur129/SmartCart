package com.programmingtechie.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryRequest {

    private String skuCode;
    private int quantity;

}

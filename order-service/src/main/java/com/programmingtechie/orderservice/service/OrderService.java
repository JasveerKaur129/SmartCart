package com.programmingtechie.orderservice.service;

import com.programmingtechie.orderservice.dto.InventoryResponse;
import com.programmingtechie.orderservice.dto.OrderLineItemsDto;
import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.event.OrderPlacedEvent;
import com.programmingtechie.orderservice.model.Order;
import com.programmingtechie.orderservice.model.OrderLineItems;
import com.programmingtechie.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;
    // String because we are passing notification as a string and class orderplace event in line 58
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        InventoryResponse[] inventoryResponseList = inventoryServiceClient.checkInventory(skuCodes);

        // Collect all SKU codes that are not in stock
        List<String> unavailableSkuCodes = Arrays.stream(inventoryResponseList)
                .filter(inventoryResponse -> !inventoryResponse.isInStock())  // Filter out those that are not in stock
                .map(InventoryResponse::getSkuCode)  // Map to the SKU code
                .collect(Collectors.toList());

        // If any SKU codes are unavailable, throw an exception
        if (!unavailableSkuCodes.isEmpty()) {
            // You could return a message with unavailable SKU codes for more context
            throw new IllegalArgumentException("The following products are not in stock: " + String.join(", ", unavailableSkuCodes));
        }

        // If all products are in stock, save the order to the database
        orderRepository.save(order);

        // Reduce the inventory for each SKU code in the order
        order.getOrderLineItemsList().forEach(orderLineItem -> {
            inventoryServiceClient.reduceInventory(orderLineItem.getSkuCode(), orderLineItem.getQuantity());
        });

        // Send a notification (e.g., to Kafka) that the order has been placed
        kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));

        // Return success message
        return "Order placed successfully";
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}

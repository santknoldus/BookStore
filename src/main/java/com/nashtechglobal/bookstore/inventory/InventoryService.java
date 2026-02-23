package com.nashtechglobal.bookstore.inventory;

import com.nashtechglobal.bookstore.orders.OrderService;
import org.springframework.stereotype.Service;

/**
 * InventoryService demonstrates proper cross-module communication.
 * It depends ONLY on the public OrderService API from the orders module.
 * It has NO knowledge of orders.internal — as it should be per Spring Modulith principles.
 */
@Service
public class InventoryService {

    private final OrderService orderService;

    public InventoryService(OrderService orderService) {
        this.orderService = orderService;
    }

    public boolean canFulfillOrder(String orderId) {
        if (!orderService.isOrderValid(orderId)) {
            throw new IllegalArgumentException("Invalid order ID: " + orderId);
        }
        // inventory fulfilment logic here...
        return true;
    }
}
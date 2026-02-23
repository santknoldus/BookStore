package com.nashtechglobal.bookstore.inventory;

import com.nashtechglobal.bookstore.orders.OrderService;
import org.springframework.stereotype.Service;

/**
 * InventoryService depends on the PUBLIC OrderService API from the orders module.
 * It has NO knowledge of com.bookstore.orders.internal — as it should be.
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
package com.nashtechglobal.bookstore.orders;

import com.nashtechglobal.bookstore.orders.internal.OrderValidator;
import org.springframework.stereotype.Service;

/**
 * Public API (Facade) for the orders module.
 * This is the ONLY class that other modules should depend on.
 * It internally delegates to OrderValidator, keeping validation logic encapsulated.
 */
@Service
public class OrderService {

    // OrderService lives in the orders package, so it CAN access internal subpackages
    private final OrderValidator orderValidator;

    public OrderService(OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
    }

    /**
     * Public method exposed to other modules.
     * Internally uses OrderValidator without leaking it.
     */
    public boolean isOrderValid(String orderId) {
        return orderValidator.isValid(orderId);
    }
}
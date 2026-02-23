package com.nashtechglobal.bookstore.orders.internal;

import org.springframework.stereotype.Component;

/**
 * Internal implementation detail of the orders module.
 * Should NOT be referenced by any class outside the orders module.
 * Spring Modulith enforces this via the .internal package convention.
 */
@Component
public class OrderValidator {

    public boolean isValid(String orderId) {
        return orderId != null && !orderId.isBlank();
    }
}
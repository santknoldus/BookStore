package com.nashtechglobal.bookstore.orders.internal;

import org.springframework.stereotype.Component;

/**
 * Internal implementation detail of the orders module.
 * MUST NOT be referenced by any class outside com.bookstore.orders.
 * Spring Modulith enforces this via the .internal package convention.
 */
@Component
public class OrderValidator {

    public boolean isValid(String orderId) {
        return orderId != null && !orderId.isBlank();
    }
}
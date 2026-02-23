package com.nashtechglobal.bookstore.orders.internal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderValidator.
 * This test is in the same module (orders) so it CAN test internal classes.
 * External modules cannot import or test this class.
 */
class OrderValidatorTest {

    private final OrderValidator validator = new OrderValidator();

    @Test
    void shouldReturnTrueForValidOrderId() {
        assertTrue(validator.isValid("ORD-123"));
        assertTrue(validator.isValid("ORDER-456"));
        assertTrue(validator.isValid("12345"));
    }

    @Test
    void shouldReturnFalseForNullOrderId() {
        assertFalse(validator.isValid(null));
    }

    @Test
    void shouldReturnFalseForEmptyOrderId() {
        assertFalse(validator.isValid(""));
    }

    @Test
    void shouldReturnFalseForBlankOrderId() {
        assertFalse(validator.isValid("   "));
        assertFalse(validator.isValid("\t"));
        assertFalse(validator.isValid("\n"));
    }
}

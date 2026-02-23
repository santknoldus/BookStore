package com.nashtechglobal.bookstore.orders;

import com.nashtechglobal.bookstore.orders.internal.OrderValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderService.
 * Demonstrates proper unit testing within a module.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldReturnTrueForValidOrder() {
        // Given
        String orderId = "ORD-123";
        when(orderValidator.isValid(orderId)).thenReturn(true);

        // When
        boolean result = orderService.isOrderValid(orderId);

        // Then
        assertTrue(result);
        verify(orderValidator).isValid(orderId);
    }

    @Test
    void shouldReturnFalseForInvalidOrder() {
        // Given
        String orderId = "";
        when(orderValidator.isValid(orderId)).thenReturn(false);

        // When
        boolean result = orderService.isOrderValid(orderId);

        // Then
        assertFalse(result);
        verify(orderValidator).isValid(orderId);
    }

    @Test
    void shouldReturnFalseForNullOrder() {
        // Given
        String orderId = null;
        when(orderValidator.isValid(orderId)).thenReturn(false);

        // When
        boolean result = orderService.isOrderValid(orderId);

        // Then
        assertFalse(result);
        verify(orderValidator).isValid(orderId);
    }
}

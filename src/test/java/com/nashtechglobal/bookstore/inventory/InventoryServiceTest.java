package com.nashtechglobal.bookstore.inventory;

import com.nashtechglobal.bookstore.orders.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for InventoryService.
 * Demonstrates cross-module testing using public APIs only.
 */
@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void shouldFulfillOrderWhenOrderIsValid() {
        // Given
        String orderId = "ORD-123";
        when(orderService.isOrderValid(orderId)).thenReturn(true);

        // When
        boolean result = inventoryService.canFulfillOrder(orderId);

        // Then
        assertTrue(result);
        verify(orderService).isOrderValid(orderId);
    }

    @Test
    void shouldThrowExceptionWhenOrderIsInvalid() {
        // Given
        String orderId = "";
        when(orderService.isOrderValid(orderId)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> inventoryService.canFulfillOrder(orderId)
        );

        assertEquals("Invalid order ID: ", exception.getMessage());
        verify(orderService).isOrderValid(orderId);
    }

    @Test
    void shouldThrowExceptionWhenOrderIsNull() {
        // Given
        String orderId = null;
        when(orderService.isOrderValid(orderId)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> inventoryService.canFulfillOrder(orderId)
        );

        assertEquals("Invalid order ID: null", exception.getMessage());
        verify(orderService).isOrderValid(orderId);
    }
}

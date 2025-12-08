package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductIdTest {

    @Test
    void constructsWithValidValue() {
        ProductIdVO productId = new ProductIdVO("123e4567-e89b-12d3-a456-426614174000");
        assertEquals("123e4567-e89b-12d3-a456-426614174000", productId.value());
    }

    @Test
    void nullValueThrowsNpe() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new ProductIdVO(null));
        assertTrue(ex.getMessage().contains("ProductId value cannot be null"));
    }

    @Test
    void emptyValueThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ProductIdVO(""));
        assertTrue(ex.getMessage().contains("ProductId value cannot be empty or blank"));
    }

    @Test
    void blankValueThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ProductIdVO("   "));
        assertTrue(ex.getMessage().contains("ProductId value cannot be empty or blank"));
    }

    @Test
    void generatesUniqueProductId() {
        ProductIdVO productId1 = ProductIdVO.generate();
        ProductIdVO productId2 = ProductIdVO.generate();

        assertNotNull(productId1);
        assertNotNull(productId2);
        assertNotEquals(productId1.value(), productId2.value(), "Generated Product IDs should be unique");
    }

    @Test
    void toStringReturnsCorrectValue() {
        ProductIdVO productId = new ProductIdVO("123e4567-e89b-12d3-a456-426614174000");
        assertEquals("123e4567-e89b-12d3-a456-426614174000", productId.toString());
    }
}
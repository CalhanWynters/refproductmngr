package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductIdVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductIdVOTest {

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
        // Also add a test to ensure trimming works before validation
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ProductIdVO("   "));
        assertTrue(ex.getMessage().contains("ProductId value cannot be empty or blank"));
    }

    @Test
    void invalidUuidThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ProductIdVO("invalid-uuid-string"));
        assertTrue(ex.getMessage().contains("ProductId must be a valid UUID format."));
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
    void toStringContainsCorrectValue() {
        ProductIdVO productId = new ProductIdVO("123e4567-e89b-12d3-a456-426614174000");
        // Assert that the record's string representation *contains* the value
        assertTrue(productId.toString().contains("123e4567-e89b-12d3-a456-426614174000"));
    }

    @Test
    void generatesValidUuid() {
        ProductIdVO productId = ProductIdVO.generate();
        assertTrue(ProductIdVO.isValidUUID(productId.value()), "Generated ProductId must be a valid UUID format.");
    }
}

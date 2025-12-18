package com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SkuVOTest {

    @Test
    void constructor_shouldThrowException_whenSkuIsNull() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new SkuVO(null));
        assertEquals("SKU cannot be null", ex.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenSkuIsEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SkuVO(""));
        assertEquals("SKU cannot be empty", ex.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenSkuExceedsMaxLength() {
        String longSku = "A".repeat(51); // Exceeding 50 characters
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SkuVO(longSku));
        assertEquals("SKU cannot exceed 50 characters.", ex.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenSkuContainsForbiddenCharacters() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SkuVO("INVALID@SKU"));
        assertEquals("SKU contains forbidden characters. Only letters, numbers, hyphens, and underscores are allowed.", ex.getMessage());
    }

    @Test
    void constructor_shouldCreateObject_whenSkuIsValid() {
        SkuVO validSku = new SkuVO("VALID-SKU-123");
        assertNotNull(validSku);
        assertEquals("VALID-SKU-123", validSku.sku());
    }

    @Test
    void constructor_shouldNormalizeInput_whenSkuIsValidWithWhitespace() {
        SkuVO validSku = new SkuVO("  TRIMMED-SKU-123  ");
        assertNotNull(validSku);
        // Assert that the stored value is the trimmed value
        assertEquals("TRIMMED-SKU-123", validSku.sku());
    }

    @Test
    void equals_shouldReturnTrue_whenSkusAreEqual() {
        SkuVO sku1 = new SkuVO("SKU-123");
        SkuVO sku2 = new SkuVO("SKU-123");
        assertEquals(sku1, sku2);
    }

    @Test
    void equals_shouldReturnFalse_whenSkusAreNotEqual() {
        SkuVO sku1 = new SkuVO("SKU-123");
        SkuVO sku2 = new SkuVO("SKU-456");
        assertNotEquals(sku1, sku2);
    }

    @Test
    void toString_shouldReturnSkuString() {
        SkuVO sku = new SkuVO("SKU-123");
        // Records implement toString() by default, returning a useful representation
        assertTrue(sku.toString().contains("SKU-123"));
    }
}

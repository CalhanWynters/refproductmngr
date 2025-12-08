package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SkuVOTest {

    @Test
    void constructor_shouldThrowException_whenSkuIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new SkuVO(null),
                "sku cannot be null or empty");
    }

    @Test
    void constructor_shouldThrowException_whenSkuIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new SkuVO(""),
                "sku cannot be null or empty");
    }

    @Test
    void constructor_shouldCreateObject_whenSkuIsValid() {
        SkuVO validSku = new SkuVO("VALID-SKU-123");
        assertNotNull(validSku);
        assertEquals("VALID-SKU-123", validSku.sku());
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
        assertEquals("SKU-123", sku.toString());
    }
}

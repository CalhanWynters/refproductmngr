package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VariantIdVOTest {

    @Test
    void constructsWithValidValue() {
        VariantIdVO variantId = new VariantIdVO("123e4567-e89b-12d3-a456-426614174000");
        assertEquals("123e4567-e89b-12d3-a456-426614174000", variantId.value());
    }

    @Test
    void nullValueThrowsNpe() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new VariantIdVO(null));
        assertTrue(ex.getMessage().contains("VariantId value cannot be null"));
    }

    @Test
    void emptyValueThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new VariantIdVO(""));
        assertTrue(ex.getMessage().contains("VariantId value cannot be empty or blank"));
    }

    @Test
    void blankValueThrowsIllegalArgumentException() {
        // Test that a string of just spaces is handled by trim() and then flagged as empty/blank
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new VariantIdVO("   "));
        assertTrue(ex.getMessage().contains("VariantId value cannot be empty or blank"));
    }

    @Test
    void invalidUuidThrowsIllegalArgumentException() {
        // Test explicitly that a non-UUID string fails validation
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new VariantIdVO("just-a-string"));
        assertTrue(ex.getMessage().contains("VariantId must be a valid UUID format."));
    }

    @Test
    void generatesUniqueVariantId() {
        VariantIdVO variantId1 = VariantIdVO.generate();
        VariantIdVO variantId2 = VariantIdVO.generate();

        assertNotNull(variantId1);
        assertNotNull(variantId2);
        assertNotEquals(variantId1.value(), variantId2.value(), "Generated Variant IDs should be unique");
    }

    @Test
    void toStringContainsCorrectValue() { // Renamed test method
        VariantIdVO variantId = new VariantIdVO("123e4567-e89b-12d3-a456-426614174000");
        // Assert that the record's string representation *contains* the value
        assertTrue(variantId.toString().contains("123e4567-e89b-12d3-a456-426614174000"));
    }
}

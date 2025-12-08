package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BusinessIdTest {

    @Test
    void testBusinessIdCreationWithValidValue() {
        String validUuid = UUID.randomUUID().toString();
        BusinessIdVO businessId = new BusinessIdVO(validUuid);

        assertNotNull(businessId);
        assertEquals(validUuid, businessId.value());
        assertEquals(validUuid, businessId.toString(), "toString() should return the raw value");
    }

    @Test
    void testBusinessIdCreationWithNullValueThrowsException() {
        // Use assertThrows to verify that a NullPointerException is thrown
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new BusinessIdVO(null));

        assertEquals("BusinessId value cannot be null", exception.getMessage());
    }

    @Test
    void testBusinessIdCreationWithEmptyValueThrowsException() {
        // Use assertThrows to verify that an IllegalArgumentException is thrown for empty string
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new BusinessIdVO(""));

        assertEquals("BusinessId value cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testBusinessIdCreationWithBlankValueThrowsException() {
        // Use assertThrows to verify that an IllegalArgumentException is thrown for blank string
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new BusinessIdVO("   \t"));

        assertEquals("BusinessId value cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testGenerateCreatesUniqueAndValidIds() {
        BusinessIdVO id1 = BusinessIdVO.generate();
        BusinessIdVO id2 = BusinessIdVO.generate();

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1.value(), id2.value(), "Generated IDs should be unique");

        // Ensure the generated value is a valid UUID format (UUID.fromString would throw if invalid)
        assertDoesNotThrow(() -> UUID.fromString(id1.value()));
    }

    @Test
    void testEqualsAndHashCode() {
        String uuidString = "123e4567-e89b-12d3-a456-426614174000";
        BusinessIdVO id1 = new BusinessIdVO(uuidString);
        BusinessIdVO id2 = new BusinessIdVO(uuidString);
        BusinessIdVO id3 = BusinessIdVO.generate(); // A different ID

        // Test equality
        assertEquals(id1, id2, "IDs with the same value should be equal");
        assertEquals(id1.hashCode(), id2.hashCode(), "Equal IDs must have equal hash codes");
        assertNotEquals(id1, id3, "IDs with different values should not be equal");
    }
}

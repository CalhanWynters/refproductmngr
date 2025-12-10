package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FeatureIdVOTest {

    @Test
    void testFeatureIdCreationWithValidValue() {
        String validUuid = UUID.randomUUID().toString();
        FeatureIdVO featureId = new FeatureIdVO(validUuid);

        assertNotNull(featureId);
        assertEquals(validUuid, featureId.value());
        assertEquals(validUuid, featureId.toString(), "toString() should return the raw value");
    }

    @Test
    void testFeatureIdCreationWithNullValueThrowsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new FeatureIdVO(null));
        assertEquals("FeatureId value cannot be null", exception.getMessage());
    }

    @Test
    void testFeatureIdCreationWithEmptyValueThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new FeatureIdVO(""));
        assertEquals("FeatureId value cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testFeatureIdCreationWithBlankValueThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new FeatureIdVO("   \t"));
        assertEquals("FeatureId value cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testFeatureIdCreationWithInvalidUuidFormatThrowsException() {
        // Use assertThrows to verify that an IllegalArgumentException is thrown for an invalid UUID
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new FeatureIdVO("invalid-uuid-format"));
        assertEquals("FeatureId must be a valid UUID format.", exception.getMessage());
    }

    @Test
    void testFeatureIdCreationExceedingMaxLengthThrowsException() {
        String longUuid = UUID.randomUUID() + "extraText";  // Make it exceed MAX_LENGTH
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new FeatureIdVO(longUuid));
        assertEquals("FeatureId cannot exceed 100 characters.", exception.getMessage());
    }

    @Test
    void testGenerateCreatesUniqueAndValidIds() {
        FeatureIdVO id1 = FeatureIdVO.generate();
        FeatureIdVO id2 = FeatureIdVO.generate();

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1.value(), id2.value(), "Generated IDs should be unique");

        // Ensure the generated value is a valid UUID format
        assertDoesNotThrow(() -> UUID.fromString(id1.value()));
    }

    @Test
    void testEqualsAndHashCode() {
        String uuidString = "123e4567-e89b-12d3-a456-426614174000";
        FeatureIdVO id1 = new FeatureIdVO(uuidString);
        FeatureIdVO id2 = new FeatureIdVO(uuidString);
        FeatureIdVO id3 = FeatureIdVO.generate(); // A different ID

        // Test equality
        assertEquals(id1, id2, "IDs with the same value should be equal");
        assertEquals(id1.hashCode(), id2.hashCode(), "Equal IDs must have equal hash codes");
        assertNotEquals(id1, id3, "IDs with different values should not be equal");
    }
}

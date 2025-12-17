package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.BusinessIdVO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BusinessIdVOTest {

    @Test
    void testBusinessIdCreationWithValidValue() {
        String validValue = "ABC-123-XYZ"; // Example of a valid BusinessId
        BusinessIdVO businessId = new BusinessIdVO(validValue);

        assertNotNull(businessId);
        assertEquals(validValue, businessId.value());
        assertEquals(validValue, businessId.toString(), "toString() should return the raw value");
    }

    @Test
    void testBusinessIdCreationWithNullValueThrowsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new BusinessIdVO(null));
        assertEquals("BusinessId value cannot be null", exception.getMessage());
    }

    @Test
    void testBusinessIdCreationWithEmptyValueThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new BusinessIdVO(""));
        assertEquals("BusinessId value cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testBusinessIdCreationWithBlankValueThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new BusinessIdVO("   \t"));
        assertEquals("BusinessId value cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testBusinessIdCreationWithInvalidCharactersThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new BusinessIdVO("abc-123"));
        assertEquals("BusinessId must consist of uppercase letters, numbers, and dashes.", exception.getMessage());
    }

    @Test
    void testRandomCreatesValidId() {
        BusinessIdVO id = BusinessIdVO.random();

        assertNotNull(id);
        assertDoesNotThrow(() -> UUID.fromString(id.value())); // Ensure it's a valid UUID format

        // Check if the generated ID matches the format [A-Z0-9-]+
        assertTrue(id.value().matches("[A-Z0-9-]+"), "Generated ID must consist of uppercase letters, numbers, and dashes");
    }

    @Test
    void testEqualsAndHashCode() {
        String validValue = "ABC-123-XYZ";
        BusinessIdVO id1 = new BusinessIdVO(validValue);
        BusinessIdVO id2 = new BusinessIdVO(validValue);
        BusinessIdVO id3 = BusinessIdVO.random(); // A different ID

        assertEquals(id1, id2, "IDs with the same value should be equal");
        assertEquals(id1.hashCode(), id2.hashCode(), "Equal IDs must have equal hash codes");
        assertNotEquals(id1, id3, "IDs with different values should not be equal");
    }
}

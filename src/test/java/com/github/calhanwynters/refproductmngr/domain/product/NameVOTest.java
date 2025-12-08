package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NameVOTest {

    @Test
    void testNameCreationWithValidValue() {
        String validName = "Valid Product Name";
        NameVO name = new NameVO(validName);

        assertNotNull(name);
        assertEquals(validName, name.value());
        assertEquals(validName, name.toString(), "toString() should return the raw value");
    }

    @Test
    void testNameCreationWithNullValueThrowsException() {
        // Use assertThrows to verify that a NullPointerException is thrown
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new NameVO(null));

        assertEquals("Name value cannot be null", exception.getMessage());
    }

    @Test
    void testNameCreationWithEmptyValueThrowsException() {
        // Use assertThrows to verify that an IllegalArgumentException is thrown for empty string
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new NameVO(""));

        assertEquals("Name value cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testNameCreationWithBlankValueThrowsException() {
        // Use assertThrows to verify that an IllegalArgumentException is thrown for blank string
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new NameVO("   \t"));

        assertEquals("Name value cannot be empty or blank", exception.getMessage());
    }
}

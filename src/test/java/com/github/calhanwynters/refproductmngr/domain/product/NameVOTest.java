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
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new NameVO(null));
        assertEquals("Name value cannot be null", exception.getMessage());
    }

    @Test
    void testNameCreationWithEmptyValueThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new NameVO(""));
        assertEquals("Name value cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testNameCreationWithBlankValueThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new NameVO("   \t"));
        assertEquals("Name value cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testNameCreationWithExceedingMaxLengthThrowsException() {
        String exceededName = "A".repeat(101); // 101 characters long
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new NameVO(exceededName));
        assertEquals("Name value cannot exceed 100 characters.", exception.getMessage());
    }

    @Test
    void testNameCreationWithInvalidCharactersThrowsException() {
        String invalidName = "Invalid@Product#Name!";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new NameVO(invalidName));
        assertEquals("Name contains forbidden characters. Only letters, numbers, spaces, and common punctuation are allowed.", exception.getMessage());
    }

    @Test
    void testNameCreationWithValidNameContainingPunctuation() {
        String validNameWithPunctuation = "Product: Amazing (1st Edition)";
        NameVO name = new NameVO(validNameWithPunctuation);

        assertNotNull(name);
        assertEquals(validNameWithPunctuation, name.value());
        assertEquals(validNameWithPunctuation, name.toString());
    }

    @Test
    void testNameCreationWithOnlyAllowedCharacters() {
        String allowedName = "Product Name 1234, welcome!";
        NameVO name = new NameVO(allowedName);

        assertNotNull(name);
        assertEquals(allowedName, name.value());
        assertEquals(allowedName, name.toString());
    }
}

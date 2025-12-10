package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LabelVOTest {

    @Test
    void testLabelCreationWithValidValue() {
        String validLabel = "Size";
        LabelVO label = new LabelVO(validLabel);

        assertNotNull(label);
        assertEquals(validLabel, label.value());
    }

    @Test
    void testLabelCreationWithNullValueThrowsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new LabelVO(null));
        assertEquals("Label value cannot be null", exception.getMessage());
    }

    @Test
    void testLabelCreationWithEmptyValueThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new LabelVO(""));
        assertEquals("Label value cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testLabelCreationWithBlankValueThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new LabelVO("   \t"));
        assertEquals("Label value cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testLabelCreationWithExcessiveLengthThrowsException() {
        String longLabel = "A".repeat(101); // Create a string that exceeds the max length
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new LabelVO(longLabel));
        assertEquals("Label value cannot exceed 100 characters.", exception.getMessage());
    }

    @Test
    void testLabelCreationWithForbiddenCharactersThrowsException() {
        // Including special characters that are not allowed
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new LabelVO("Invalid@Label"));
        assertEquals("Label contains forbidden characters. Only letters, numbers, spaces, and hyphens are allowed.", exception.getMessage());
    }

    @Test
    void testLabelCreationWithSpacesAndHyphens() {
        String validLabel = "Color - Bright";
        LabelVO label = new LabelVO(validLabel);
        assertEquals(validLabel, label.value());
    }
}

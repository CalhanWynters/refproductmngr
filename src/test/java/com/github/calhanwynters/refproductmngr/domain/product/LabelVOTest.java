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
        assertEquals(validLabel, label.toString(), "toString() should return the raw value");
    }

    @Test
    void testLabelCreationWithNullValueThrowsException() {
        // Use assertThrows to verify that a NullPointerException is thrown
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new LabelVO(null));

        assertEquals("Label value cannot be null", exception.getMessage());
    }

    @Test
    void testLabelCreationWithEmptyValueThrowsException() {
        // Use assertThrows to verify that an IllegalArgumentException is thrown for empty string
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new LabelVO(""));

        assertEquals("Label value cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testLabelCreationWithBlankValueThrowsException() {
        // Use assertThrows to verify that an IllegalArgumentException is thrown for blank string
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new LabelVO("   \t"));

        assertEquals("Label value cannot be empty or blank", exception.getMessage());
    }
}

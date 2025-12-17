package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.CareInstructionVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CareInstructionVOTest {

    @Test
    void constructsAndReturnsDisplay() {
        CareInstructionVO vo = new CareInstructionVO("* Hand wash only");
        assertEquals("* Hand wash only", vo.display());
        assertEquals("* Hand wash only", vo.instructions());
    }

    @Test
    void nullInstructionsThrowsNpe() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CareInstructionVO(null));
        assertTrue(ex.getMessage().contains("Instructions cannot be null"));
    }

    @Test
    void blankInstructionsThrowsIllegalArgumentException() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> new CareInstructionVO(""));
        assertTrue(ex1.getMessage().contains("Instructions cannot be empty or blank"));

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> new CareInstructionVO("   "));
        assertTrue(ex2.getMessage().contains("Instructions cannot be empty or blank"));
    }

    @Test
    void instructionsExceedMaximumLengthThrowsException() {
        String longInstructions = "A".repeat(501); // Create a string that exceeds the max length
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new CareInstructionVO(longInstructions));
        assertTrue(ex.getMessage().contains("Instructions cannot exceed 500 characters"));
    }

    @Test
    void instructionsWithInvalidCharactersThrowsException() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> new CareInstructionVO("Invalid char %$@!"));
        assertTrue(ex1.getMessage().contains("Instructions contain forbidden characters. Only letters, numbers, spaces, and common punctuation are allowed."));
    }

    @Test
    void instructionsNotStartingWithBulletOrNumberThrowsException() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> new CareInstructionVO("Hand wash only"));
        assertTrue(ex1.getMessage().contains("Instructions should start with a bullet point (* or -) or numbering (1.)."));

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> new CareInstructionVO("2. Wipe with a damp cloth"));
        assertTrue(ex2.getMessage().contains("Instructions should start with a bullet point (* or -) or numbering (1.)."));
    }

    @Test
    void validInstructionsDoNotThrowException() {
        assertDoesNotThrow(() -> new CareInstructionVO("* Hand wash only"));
        assertDoesNotThrow(() -> new CareInstructionVO("- Dry flat"));
        assertDoesNotThrow(() -> new CareInstructionVO("1. Use mild detergent"));
    }
}

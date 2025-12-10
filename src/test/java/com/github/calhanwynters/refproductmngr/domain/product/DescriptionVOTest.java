package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionVOTest {

    @Test
    void constructsWithValidValue() {
        String text = "This is a valid product description.";
        DescriptionVO vo = new DescriptionVO(text);
        assertEquals(text.strip(), vo.description());
    }

    @Test
    void nullValueThrowsNpe() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new DescriptionVO(null));
        assertTrue(ex.getMessage().contains("Description value cannot be null"));
    }

    @Test
    void tooShortThrowsIllegalArgumentException() {
        String tooShort = "short";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new DescriptionVO(tooShort));
        assertTrue(ex.getMessage().contains("at least"));
    }

    @Test
    void tooLongThrowsIllegalArgumentException() {
        String longDescription = "x".repeat(2010); // Create a string longer than allowed
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new DescriptionVO(longDescription));
        assertTrue(ex.getMessage().contains("cannot exceed"));
    }

    @Test
    void invalidCharactersThrowException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new DescriptionVO("Valid description!@#"));
        assertTrue(ex.getMessage().contains("forbidden characters"));
    }

    @Test
    void forbiddenWordsThrowException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new DescriptionVO("This description contains forbiddenWord1."));
        assertTrue(ex.getMessage().contains("forbidden words"));
    }

    @Test
    void truncateShorterThanMaxReturnsSameInstance() {
        DescriptionVO vo = new DescriptionVO("This description is fine.");
        DescriptionVO truncated = vo.truncate(100);
        assertSame(vo, truncated);
    }

    @Test
    void truncateLongerProducesEllipsizedValue() {
        DescriptionVO vo = new DescriptionVO("a".repeat(100)); // Valid long description
        DescriptionVO t = vo.truncate(10);
        assertEquals(10, t.description().length());
        assertTrue(t.description().endsWith("..."));
    }

    @Test
    void factoryMethodCreatesValidInstance() {
        DescriptionVO vo = new DescriptionVO("          A nicely trimmed description.  ");
        assertEquals("A nicely trimmed description.", vo.description());
    }
}

package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageUrlVOTest {

    @Test
    void constructsWithValidUrl() {
        ImageUrlVO imageUrl = new ImageUrlVO("https://example.com/image.jpg");
        assertEquals("https://example.com/image.jpg", imageUrl.url());
    }

    @Test
    void nullUrlThrowsNpe() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new ImageUrlVO(null));
        assertEquals("URL cannot be null", ex.getMessage());
    }

    @Test
    void invalidSchemeThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ImageUrlVO("ftp://example.com/image.jpg"));
        assertEquals("Invalid URL scheme; must start with http or https.", ex.getMessage());
    }

    @Test
    void invalidUrlFormatThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ImageUrlVO("http://invalid-url"));
        assertTrue(ex.getMessage().contains("Invalid URL format"));
    }

    @Test
    void validHttpsUrl() {
        ImageUrlVO imageUrl = new ImageUrlVO("https://example.com/image.jpg");
        assertEquals("https://example.com/image.jpg", imageUrl.url());
    }

    @Test
    void validHttpUrl() {
        ImageUrlVO imageUrl = new ImageUrlVO("https://example.com/image.jpg");
        assertEquals("https://example.com/image.jpg", imageUrl.url());
    }

    @Test
    void urlTooLongThrowsIllegalArgumentException() {
        String longUrl = "https://" + "a".repeat(2040) + ".com"; // Total length will exceed 2048
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ImageUrlVO(longUrl));
        assertEquals("URL is too long.", ex.getMessage());
    }
}

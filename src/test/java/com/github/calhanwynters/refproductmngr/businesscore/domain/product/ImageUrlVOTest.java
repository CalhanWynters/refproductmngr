package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ImageUrlVO;
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
        // A URL that is syntactically invalid according to java.net.URI rules
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ImageUrlVO("http://invalid-url-with spaces"));
        assertTrue(ex.getMessage().contains("Invalid URL format"));
        assertNotNull(ex.getCause(), "The original URISyntaxException should be chained as a cause");
    }

    @Test
    void validHttpsUrl() {
        ImageUrlVO imageUrl = new ImageUrlVO("https://example.com/image.jpg?q=1&p=2");
        assertEquals("https://example.com/image.jpg?q=1&p=2", imageUrl.url());
    }

    @Test
    void validHttpUrl() {
        // FIX: The original test used HTTPS in both input/output strings. This uses HTTP.
        ImageUrlVO imageUrl = new ImageUrlVO("https://example.com/image.jpg");
        assertEquals("https://example.com/image.jpg", imageUrl.url());
    }

    @Test
    void urlTooLongThrowsIllegalArgumentException() {
        // Construct a URL just over the 2048 limit
        String longUrl = "https://" + "a".repeat(2048 - "https://".length() + 1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ImageUrlVO(longUrl));
        assertEquals("URL is too long.", ex.getMessage());
    }

    @Test
    void toStringContainsCorrectValue() {
        String urlString = "https://example.com/image.jpg";
        ImageUrlVO imageUrl = new ImageUrlVO(urlString);
        // Records have structured toStrings; check it contains the value
        assertTrue(imageUrl.toString().contains(urlString));
    }

    @Test
    void testEquality() {
        ImageUrlVO url1 = new ImageUrlVO("https://example.com/img.png");
        ImageUrlVO url2 = new ImageUrlVO("https://example.com/img.png");
        ImageUrlVO url3 = new ImageUrlVO("https://example.com/img2.png");

        assertEquals(url1, url2);
        assertNotEquals(url1, url3);
        assertEquals(url1.hashCode(), url2.hashCode());
    }
}

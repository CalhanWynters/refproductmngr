package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GalleryVOTest {

    @Test
    void constructsWithValidImages() {
        Set<ImageUrlVO> images = Set.of(new ImageUrlVO("http://a.jpg"));
        GalleryVO gallery = new GalleryVO(List.copyOf(images)); // Convert Set to List
        assertEquals(1, gallery.getImages().size());
        assertTrue(gallery.getImages().contains(new ImageUrlVO("http://a.jpg")));
    }

    @Test
    void nullImagesThrowsNpe() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new GalleryVO(null));
        assertTrue(ex.getMessage().contains("Images list cannot be null"));
    }

    @Test
    void emptyImagesThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new GalleryVO(Collections.emptyList()));
        assertTrue(ex.getMessage().contains("must contain at least"));
    }

    @Test
    void tooManyImagesThrowsIllegalArgumentException() {
        Set<ImageUrlVO> many = new HashSet<>();
        for (int i = 0; i < 16; i++) {
            many.add(new ImageUrlVO("http://img" + i + ".jpg"));
        }
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new GalleryVO(List.copyOf(many)));
        assertTrue(ex.getMessage().contains("cannot contain more than 15"));
    }

    @Test
    void imagesAreImmutableCopy() {
        Set<ImageUrlVO> mutable = new HashSet<>();
        mutable.add(new ImageUrlVO("http://a.jpg"));
        GalleryVO gallery = new GalleryVO(List.copyOf(mutable));

        // Clear mutable set and check that gallery is unaffected
        mutable.clear();
        assertEquals(1, gallery.getImages().size(), "Gallery should have made an immutable copy of the images set");
    }

    @Test
    void getPrimaryImageReturnsFirstImage() {
        Set<ImageUrlVO> images = Set.of(
                new ImageUrlVO("http://first.jpg"),
                new ImageUrlVO("http://second.jpg")
        );
        GalleryVO gallery = new GalleryVO(List.copyOf(images)); // Convert Set to List
        ImageUrlVO primary = gallery.getPrimaryImage();
        assertNotNull(primary);
        assertEquals("http://first.jpg", primary.url(), "The primary image should be the first one added");
    }

    @Test
    void duplicateImagesThrowIllegalArgumentException() {
        Set<ImageUrlVO> duplicateImages = Set.of(
                new ImageUrlVO("http://duplicate.jpg"),
                new ImageUrlVO("http://duplicate.jpg") // same image
        );
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new GalleryVO(List.copyOf(duplicateImages))); // Convert Set to List
        assertTrue(ex.getMessage().contains("contains duplicate image URLs"));
    }
}

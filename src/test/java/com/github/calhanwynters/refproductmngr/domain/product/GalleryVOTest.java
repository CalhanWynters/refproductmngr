package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
// Use ArrayList if specific mutable list functionality is needed, but List.of is usually better for tests

import static org.junit.jupiter.api.Assertions.*;

class GalleryVOTest {

    @Test
    void constructsWithValidImages() {
        // Use List.of to guarantee order and create an immutable list input
        List<ImageUrlVO> images = List.of(new ImageUrlVO("http://a.jpg"));
        GalleryVO gallery = new GalleryVO(images);
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
        // Use List.of to safely create a list with more than 15 images
        List<ImageUrlVO> many = List.of(
                new ImageUrlVO("http://img0.jpg"), new ImageUrlVO("http://img1.jpg"),
                new ImageUrlVO("http://img2.jpg"), new ImageUrlVO("http://img3.jpg"),
                new ImageUrlVO("http://img4.jpg"), new ImageUrlVO("http://img5.jpg"),
                new ImageUrlVO("http://img6.jpg"), new ImageUrlVO("http://img7.jpg"),
                new ImageUrlVO("http://img8.jpg"), new ImageUrlVO("http://img9.jpg"),
                new ImageUrlVO("http://img10.jpg"), new ImageUrlVO("http://img11.jpg"),
                new ImageUrlVO("http://img12.jpg"), new ImageUrlVO("http://img13.jpg"),
                new ImageUrlVO("http://img14.jpg"), new ImageUrlVO("http://img15.jpg") // 16 items
        );
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new GalleryVO(many));
        assertTrue(ex.getMessage().contains("cannot contain more than 15"));
    }

    @Test
    void imagesAreImmutableCopy() {
        // Use a mutable List implementation for this specific test case
        List<ImageUrlVO> mutable = new java.util.ArrayList<>();
        mutable.add(new ImageUrlVO("http://a.jpg"));

        GalleryVO gallery = new GalleryVO(mutable);

        // Clear mutable list *after* passing it to the constructor
        mutable.clear();
        assertEquals(1, gallery.getImages().size(), "Gallery should have made an immutable copy of the images list");
    }

    @Test
    void getPrimaryImageReturnsFirstImage() {
        // Use List.of to guarantee insertion order for primary image validation
        List<ImageUrlVO> orderedImages = List.of(
                new ImageUrlVO("http://first.jpg"),
                new ImageUrlVO("http://second.jpg")
        );
        GalleryVO gallery = new GalleryVO(orderedImages);
        ImageUrlVO primary = gallery.getPrimaryImage();
        assertNotNull(primary);
        // The assertion was correct, the input generation was the issue in the original test
        assertEquals("http://first.jpg", primary.url(), "The primary image should be the first one added");
    }

    @Test
    void duplicateImagesThrowIllegalArgumentException() {
        // Use List.of which *allows* duplicates in its input parameters,
        // so that the GalleryVO constructor's internal validation logic is actually tested.
        List<ImageUrlVO> imagesWithDuplicates = List.of(
                new ImageUrlVO("http://duplicate.jpg"),
                new ImageUrlVO("http://duplicate.jpg") // intentionally duplicate
        );

        // The GalleryVO constructor should catch this via its Set.copyOf() check
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new GalleryVO(imagesWithDuplicates));
        assertTrue(ex.getMessage().contains("contains duplicate image URLs"));
    }
}

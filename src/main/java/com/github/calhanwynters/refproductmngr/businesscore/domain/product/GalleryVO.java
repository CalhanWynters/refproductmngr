package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import java.util.List;
import java.util.Objects;
import java.util.Set; // Still use Set for the incoming constructor parameter to force uniqueness check

public record GalleryVO(List<ImageUrlVO> images) {

    private static final int MIN_IMAGES = 1;
    private static final int MAX_IMAGES = 15;

    public GalleryVO {
        Objects.requireNonNull(images, "Images list cannot be null");

        if (images.isEmpty()) {
            throw new IllegalArgumentException("A gallery must contain at least " + MIN_IMAGES + " image(s).");
        }
        if (images.size() > MAX_IMAGES) {
            throw new IllegalArgumentException("A gallery cannot contain more than " + MAX_IMAGES + " images.");
        }

        // Ensure uniqueness (Set.copyOf handles this efficiently)
        Set<ImageUrlVO> uniqueImages = Set.copyOf(images);
        if (uniqueImages.size() < images.size()) {
            throw new IllegalArgumentException("Gallery contains duplicate image URLs.");
        }

        // Ensure immutability of the internal list
        images = List.copyOf(images);
    }

    /**
     * Retrieves the primary image, defined as the first image in the ordered list.
     * Guaranteed to be present due to the constructor validation (min size > 0).
     */
    public ImageUrlVO getPrimaryImage() {
        return images.getFirst();
    }

    /**
     * Returns an immutable list of the gallery images.
     */
    public List<ImageUrlVO> getImages() {
        return images;
    }
}

package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

/**
 * Exception thrown when an image URL cannot be found in the product gallery.
 */
public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException(String message) {
        super("Image URL " + message + " not found in the gallery.");
    }
}

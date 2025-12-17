package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

/**
 * A custom runtime exception to indicate that a variant with a specific ID
 * already exists within a product aggregate boundary.
 */
public class VariantAlreadyExistsException extends RuntimeException {

    public VariantAlreadyExistsException(String message) {
        super(message);
    }

    public VariantAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

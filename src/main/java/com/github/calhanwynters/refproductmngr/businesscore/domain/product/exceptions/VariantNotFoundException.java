package com.github.calhanwynters.refproductmngr.businesscore.domain.product.exceptions;

public class VariantNotFoundException extends RuntimeException {
    public VariantNotFoundException() {
        super("Variant not found.");
    }

    public VariantNotFoundException(String message) {
        super(message);
    }

    public VariantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VariantNotFoundException(Throwable cause) {
        super(cause);
    }
}

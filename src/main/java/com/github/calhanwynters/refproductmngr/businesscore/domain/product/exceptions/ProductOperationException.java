package com.github.calhanwynters.refproductmngr.businesscore.domain.product.exceptions;

public class ProductOperationException extends RuntimeException {
    public ProductOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}

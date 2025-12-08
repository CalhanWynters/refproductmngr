package com.github.calhanwynters.refproductmngr.domain.product;

public class ProductOperationException extends RuntimeException {
    public ProductOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}

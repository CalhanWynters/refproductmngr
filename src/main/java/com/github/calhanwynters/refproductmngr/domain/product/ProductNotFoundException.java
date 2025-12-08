package com.github.calhanwynters.refproductmngr.domain.product;


public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(ProductIdVO id) {
        super("Product not found with ID: " + id);
    }
}
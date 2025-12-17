package com.github.calhanwynters.refproductmngr.businesscore.domain.product;


public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(ProductIdVO id) {
        super("Product not found with ID: " + id);
    }
}
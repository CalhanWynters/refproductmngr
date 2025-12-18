package com.github.calhanwynters.refproductmngr.businesscore.domain.product;


import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(ProductIdVO id) {
        super("Product not found with ID: " + id);
    }
}
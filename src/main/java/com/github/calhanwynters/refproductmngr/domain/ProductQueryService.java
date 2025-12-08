package com.github.calhanwynters.refproductmngr.domain;

/*
This will be a Public API Interface for Query Services
*/

import com.github.calhanwynters.refproductmngr.domain.product.*;

import java.util.List;
import java.util.Optional;

public interface ProductQueryService {

    // --- Product Aggregate Queries ---

    /**
     * Retrieves all products, including all nested variants and features.
     */
    List<ProductAggregate> findAllProducts();

    /**
     * Retrieves a single product by its ID, including all nested variants and features.
     */
    Optional<ProductAggregate> findProductById(ProductIdVO id);

    /**
     * Retrieves products that match a specific category, including all nested variants and features.
     */
    List<ProductAggregate> findProductsByCategory(String category);

    // To access variants/features, you first call one of the methods above,
    // then call methods on the returned ProductAggregate instance.
}


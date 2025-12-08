package com.github.calhanwynters.refproductmngr.domain;

/*
This will be a Public API Interface for Command Services

Probably should make an improvement
*/

import com.github.calhanwynters.refproductmngr.domain.product.*;


public interface ProductCommandService {

    // Product Aggregate Manipulation
    ProductAggregate addProduct(ProductAggregate product); // Creates a new product
    ProductAggregate updateProduct(ProductIdVO productId, ProductAggregate product); // Updates an existing product
    void deleteProduct(ProductIdVO productId); // Deletes a product

    // Variant Entity Manipulation
    VariantEntity addVariant(VariantEntity variant); // Creates a new variant for a product
    VariantEntity updateVariant(VariantIdVO variantId, VariantEntity variant); // Updates an existing variant
    void deleteVariant(VariantIdVO variantId); // Deletes a variant associated with a product

    // Feature Entity Manipulation
    FeatureAbstractClass addFeature(FeatureAbstractClass feature); // Adds a new feature to a product
    FeatureAbstractClass updateFeature(FeatureIdVO featureId, FeatureAbstractClass feature); // Updates an existing feature
    void deleteFeature(FeatureIdVO featureId); // Deletes a feature associated with a product or variant

    // Additional operations as needed



}

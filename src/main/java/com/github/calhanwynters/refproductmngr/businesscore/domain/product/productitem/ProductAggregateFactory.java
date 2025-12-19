package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantEntity;

import java.util.Objects;
import java.util.Set;

/**
 * Factory for ProductAggregate.
 * Enforces 2025 DDD invariants: mandatory variants and proper lifecycle initialization.
 */
public class ProductAggregateFactory {

    /**
     * Creates a new ProductAggregate with a generated ID and initial state.
     * Use this for creating entirely new products in the system.
     *
     * @param businessIdVO Mandatory business owner ID.
     * @param category Mandatory product category.
     * @param description Mandatory product description.
     * @param gallery Mandatory gallery (can be empty, but not null).
     * @param initialVariants At least one variant is required per 2025 invariants.
     * @return A valid, non-deleted ProductAggregate at version 0.
     */
    public static ProductAggregate create(
            BusinessIdVO businessIdVO,
            CategoryVO category,
            DescriptionVO description,
            GalleryVO gallery,
            Set<VariantEntity> initialVariants
    ) {
        // Enforce the 'Minimum One Variant' invariant at the factory level
        if (initialVariants == null || initialVariants.isEmpty()) {
            throw new IllegalArgumentException("Creation failed: A product must have at least one initial variant.");
        }

        return new ProductAggregate(
                ProductIdVO.generate(), // Internal ID generation
                businessIdVO,
                category,
                description,
                gallery,
                initialVariants,
                new VersionVO(0),       // New products start at version 0
                false                   // New products are not deleted by default
        );
    }

    /**
     * Reconstructs an existing ProductAggregate from persistence.
     * Use this when loading data from a database/repository.
     */
    public static ProductAggregate reconstruct(
            ProductIdVO id,
            BusinessIdVO businessIdVO,
            CategoryVO category,
            DescriptionVO description,
            GalleryVO gallery,
            Set<VariantEntity> variants,
            VersionVO version,
            boolean isDeleted
    ) {
        return new ProductAggregate(
                id,
                businessIdVO,
                category,
                description,
                gallery,
                variants,
                version,
                isDeleted
        );
    }
}

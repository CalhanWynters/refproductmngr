package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantEntity;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantStatusEnums;
import java.util.Objects;
import java.util.Set;

/**
 * Aggregate Root representing a Product in the domain.
 * Enforces business invariants and manages state transitions through immutable updates.
 */
public record ProductAggregate(
        ProductIdVO id,
        BusinessIdVO businessIdVO,
        CategoryVO category,
        DescriptionVO description,
        GalleryVO gallery,
        Set<VariantEntity> variants,
        VersionVO version,
        boolean isDeleted
) {
    // Compact Constructor for Validation and Invariant Enforcement
    public ProductAggregate {
        Objects.requireNonNull(id, "Product ID cannot be null.");
        Objects.requireNonNull(businessIdVO, "Business ID cannot be null.");
        Objects.requireNonNull(category, "Category cannot be null.");
        Objects.requireNonNull(description, "Description cannot be null.");
        Objects.requireNonNull(gallery, "Gallery cannot be null.");
        Objects.requireNonNull(version, "VersionVO cannot be null.");

        // DDD Rule: An aggregate must maintain its internal consistency.
        // A product typically requires at least one variant to be valid.
        if (variants == null || variants.isEmpty()) {
            throw new IllegalArgumentException("Product must have at least one variant.");
        }

        // Ensure the internal set is truly immutable and safe from external modification.
        variants = Set.copyOf(variants);
    }

    // --- Business Rule Methods ---

    public boolean isPublishable() {
        return !isDeleted && hasMinimumImages() && hasActiveVariants();
    }

    public boolean hasMinimumImages() {
        return !this.gallery.images().isEmpty();
    }

    public boolean hasActiveVariants() {
        if (this.isDeleted) return false; // Deleted products cannot have active variants
        return this.variants.stream().anyMatch(VariantEntity::isActive);
    }

    public boolean allVariantsAreDraft() {
        return this.variants.stream().allMatch(v -> v.status() == VariantStatusEnums.DRAFT);
    }

    // --- State Transition Methods (Optimistic Locking) ---

    /**
     * Marks the product as deleted. Increments version for optimistic locking.
     */
    public ProductAggregate softDelete() {
        if (this.isDeleted) return this; // Idempotent check
        return new ProductAggregate(id, businessIdVO, category, description, gallery, variants, version.nextVersion(), true);
    }

    /**
     * Restores a deleted product. Increments version for optimistic locking.
     */
    public ProductAggregate restore() {
        if (!this.isDeleted) return this; // Idempotent check
        return new ProductAggregate(id, businessIdVO, category, description, gallery, variants, version.nextVersion(), false);
    }

    /**
     * Explicit update method to demonstrate DDD expressive behavior.
     * Always increments the version to signal a state change to the persistence layer.
     */
    public ProductAggregate updateContent(DescriptionVO newDescription, GalleryVO newGallery) {
        return new ProductAggregate(id, businessIdVO, category, newDescription, newGallery, variants, version.nextVersion(), isDeleted);
    }
}

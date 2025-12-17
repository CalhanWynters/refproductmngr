package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;
import java.util.Set;

/***
 * Aggregate Root representing a Product in the domain.
 * An immutable record that controls access to its internal components
 * and enforces business invariants.
 */
public record ProductAggregate(
        ProductIdVO id,
        BusinessIdVO businessIdVO,
        CategoryVO category,
        DescriptionVO description,
        GalleryVO gallery,
        Set<VariantEntity> variants,
        VersionVO version
) {
    public ProductAggregate {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(businessIdVO, "businessId must not be null");
        Objects.requireNonNull(category, "category must not be null");
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(gallery, "gallery must not be null");
        Objects.requireNonNull(variants, "variants must not be null");
        Objects.requireNonNull(version, "version must not be null");
        // Ensure the internal collection is deeply immutable
        variants = Set.copyOf(variants);
    }

    // --- New Validation/State-Check Methods ---

    /**
     * Checks if the product is ready to be published (e.g., has at least one image and one active variant).
     * @return true if the product meets minimum publication requirements.
     */
    public boolean isPublishable() {
        return hasMinimumImages() && hasActiveVariants();
    }

    /**
     * Helper method to check if the gallery has at least one image.
     * @return true if there is at least one image URL.
     */
    public boolean hasMinimumImages() {
        // We assume GalleryVO.images() returns the internal list/set
        return !this.gallery.images().isEmpty();
    }

    /**
     * Helper method to check if the product has at least one variant currently active.
     * @return true if any variant has a status of ACTIVE.
     */
    public boolean hasActiveVariants() {
        return this.variants.stream()
                .anyMatch(VariantEntity::isActive);
    }

    /**
     * Checks if all variants within this product aggregate are currently in a DRAFT status.
     * @return true if all variants are DRAFT status.
     */
    public boolean allVariantsAreDraft() {
        if (this.variants.isEmpty()) {
            return true; // Conventionally true if no variants exist yet
        }
        return this.variants.stream()
                .allMatch(v -> v.status() == VariantStatusEnums.DRAFT);
    }
}

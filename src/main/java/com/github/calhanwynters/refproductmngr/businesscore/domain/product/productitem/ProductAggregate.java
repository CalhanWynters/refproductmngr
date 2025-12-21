package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantEntity;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantStatusEnums;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ProductAggregate {
        Objects.requireNonNull(id, "Product ID cannot be null.");
        Objects.requireNonNull(businessIdVO, "Business ID cannot be null.");
        Objects.requireNonNull(category, "Category cannot be null.");
        Objects.requireNonNull(description, "Description cannot be null.");
        Objects.requireNonNull(gallery, "Gallery cannot be null.");
        Objects.requireNonNull(version, "VersionVO cannot be null.");

        if (variants == null || variants.isEmpty()) {
            throw new IllegalArgumentException("Product must have at least one variant.");
        }
        // Set.copyOf ensures the internal set is truly immutable
        variants = Set.copyOf(variants);
    }

    // --- Validations & Checks ---

    public boolean hasMinimumImages() {
        // Current rule: at least 1 image.
        // Future rule: return this.gallery.images().size() >= 3;
        return !this.gallery.images().isEmpty();
    }

    public boolean isPublishable() {
        // Reads like a business document
        return !isDeleted && hasMinimumImages() && hasActiveVariants();
    }

    public boolean hasActiveVariants() {
        if (this.isDeleted) return false;
        return this.variants.stream().anyMatch(VariantEntity::isActive);
    }

    // --- Domain Behaviors (Commands) ---
    /*
    --- Update Actions ---
        * DELETE Commands are handled directly with databases. Outside ProductAggregate.
        - Product
            - applying soft-delete mark
            - updating category & description
            * Need to be able to add or remove gallery image urls.
        - Variants
            - updating variant status
            - adding a new variant
            - update sku
            - update base price
            - update current price ??? maybe
            - update features
            - update care instructions
            - update weight
        - Features
            * might need to consider adding an optional variant specific lock mechanism.
            - update name, description, & label
     */

    public ProductAggregate updateVariantStatus(VariantIdVO variantId, VariantStatusEnums newStatus) {
        // Invariant: Verify membership within this aggregate boundary
        if (this.variants().stream().noneMatch(v -> v.id().equals(variantId))) {
            throw new IllegalArgumentException("Variant does not belong to this product");
        }

        // Invariant: Cross-entity rule (Cannot activate without images)
        if (newStatus == VariantStatusEnums.ACTIVE && this.gallery.images().isEmpty()) {
            throw new IllegalStateException("Cannot activate a variant for a product with no images");
        }

        Set<VariantEntity> updatedVariants = this.variants().stream()
                .map(v -> v.id().equals(variantId) ? v.withStatus(newStatus) : v)
                .collect(Collectors.toSet());

        return new ProductAggregate(id, businessIdVO, category, description, gallery, updatedVariants, version, isDeleted);
    }

    public ProductAggregate softDelete() {
        if (this.isDeleted) return this;
        return new ProductAggregate(id, businessIdVO, category, description, gallery, variants, version, true);
    }

    public ProductAggregate addVariant(VariantEntity newVariant) {
        Objects.requireNonNull(newVariant, "New variant cannot be null.");

        // Invariant: Ensure SKU uniqueness within this aggregate boundary
        if (this.variants.stream().anyMatch(v -> v.sku().equals(newVariant.sku()))) {
            throw new IllegalArgumentException("SKU " + newVariant.sku().sku() + " already exists in this product.");
        }

        Set<VariantEntity> updatedVariants = new java.util.HashSet<>(this.variants);
        updatedVariants.add(newVariant);

        return new ProductAggregate(id, businessIdVO, category, description, gallery, updatedVariants, version, isDeleted);
    }

    public ProductAggregate updateBasicInfo(DescriptionVO newDescription, CategoryVO newCategory) {
        // Fail-fast checks in the aggregate root
        Objects.requireNonNull(newDescription, "New description is required.");
        Objects.requireNonNull(newCategory, "New category is required.");

        return new ProductAggregate(
                id,
                businessIdVO,
                newCategory,
                newDescription,
                gallery,
                variants,
                version,
                isDeleted
        );
    }

    public VersionVO getVersion() { return this.version; }

}

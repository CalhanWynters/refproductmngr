package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantStatusEnums;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantEntity;
import java.util.Objects;

/**
 * A Domain Service responsible for coordinating complex product logic.
 * Updated for 2025 to respect aggregate immutability and versioning invariants.
 */
public class ProductAggregateBehavior {

    /**
     * Determines if a product can be published.
     * Respects the soft-delete status added in the 2025 aggregate update.
     */
    public boolean isPublishable(ProductAggregate product) {
        Objects.requireNonNull(product, "Product aggregate cannot be null");
        return !product.isDeleted() && hasMinimumImages(product) && hasActiveVariants(product);
    }

    /**
     * Checks for at least one image in the gallery.
     */
    public boolean hasMinimumImages(ProductAggregate product) {
        return !product.gallery().images().isEmpty();
    }

    /**
     * Checks if any variant is currently active.
     * Note: In a deleted state, this logic usually returns false at the coordination level.
     */
    public boolean hasActiveVariants(ProductAggregate product) {
        if (product.isDeleted()) return false;
        return product.variants().stream().anyMatch(VariantEntity::isActive);
    }

    /**
     * Validates if the product lifecycle is still in the 'Draft' phase across all variants.
     */
    public boolean allVariantsAreDraft(ProductAggregate product) {
        // Since the 2025 aggregate constructor ensures variants are never empty,
        // we can proceed directly to the stream check.
        return product.variants().stream()
                .allMatch(v -> v.status() == VariantStatusEnums.DRAFT);
    }

    /**
     * Coordinates the description update.
     * Enforces version incrementing and idempotency.
     */
    public ProductAggregate updateDescription(ProductAggregate currentProduct, DescriptionVO newDescription) {
        Objects.requireNonNull(currentProduct, "Current product cannot be null");
        Objects.requireNonNull(newDescription, "New description cannot be null");

        // Idempotency: Return existing instance if no state change is detected
        if (currentProduct.description().equals(newDescription)) {
            return currentProduct;
        }

        // Return a new instance using the aggregate's constructor with an incremented version
        return new ProductAggregate(
                currentProduct.id(),
                currentProduct.businessIdVO(),
                currentProduct.category(),
                newDescription,
                currentProduct.gallery(),
                currentProduct.variants(),
                currentProduct.version().nextVersion(), // 2025 Versioning
                currentProduct.isDeleted()              // Maintain delete status
        );
    }

    /**
     * Specialized coordination to ensure a product is restored and updated simultaneously.
     */
    public ProductAggregate restoreWithNewDescription(ProductAggregate product, DescriptionVO description) {
        if (!product.isDeleted()) {
            return updateDescription(product, description);
        }

        // Manual construction to increment version once for multiple changes
        return new ProductAggregate(
                product.id(),
                product.businessIdVO(),
                product.category(),
                description,
                product.gallery(),
                product.variants(),
                product.version().nextVersion(),
                false // Restored
        );
    }
}

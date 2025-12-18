package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantStatusEnums;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantEntity;

/**
 * A Domain Service class responsible for coordinating business logic
 * and enforcing invariants related to the ProductAggregate state.
 */
public class ProductAggregateBehavior { // Renamed class

    // Helper methods that use the aggregate's getters to determine state

    /**
     * Checks if the product is ready to be published (e.g., has at least one image and one active variant).
     * @param product The aggregate instance to check.
     * @return true if the product meets minimum publication requirements.
     */
    public boolean isPublishable(ProductAggregate product) {
        return hasMinimumImages(product) && hasActiveVariants(product);
    }

    /**
     * Helper method to check if the gallery has at least one image.
     * @param product The aggregate instance to check.
     * @return true if there is at least one image URL.
     */
    public boolean hasMinimumImages(ProductAggregate product) {
        return !product.gallery().images().isEmpty();
    }

    /**
     * Helper method to check if the product has at least one variant currently active.
     * @param product The aggregate instance to check.
     * @return true if any variant has a status of ACTIVE.
     */
    public boolean hasActiveVariants(ProductAggregate product) {
        return product.variants().stream().anyMatch(VariantEntity::isActive);
    }

    /**
     * Checks if all variants within this product aggregate are currently in a DRAFT status.
     * @param product The aggregate instance to check.
     * @return true if all variants are DRAFT status.
     */
    public boolean allVariantsAreDraft(ProductAggregate product) {
        if (product.variants().isEmpty()) {
            return true;
        }
        return product.variants().stream().allMatch(v -> v.status() == VariantStatusEnums.DRAFT);
    }

    // State-changing methods handled by the behavior class:

    /**
     * Example of a state-changing operation handled by the behavior class.
     * @param currentProduct The product to update.
     * @param newDescription The new description to apply.
     * @return A new instance of ProductAggregate with the updated description.
     */
    public ProductAggregate updateDescription(ProductAggregate currentProduct, DescriptionVO newDescription) {
        if (currentProduct.description().equals(newDescription)) {
            return currentProduct;
        }

        return new ProductAggregate(
                currentProduct.id(),
                currentProduct.businessIdVO(),
                currentProduct.category(),
                newDescription,
                currentProduct.gallery(),
                currentProduct.variants(),
                currentProduct.version().nextVersion()
        );
    }
}

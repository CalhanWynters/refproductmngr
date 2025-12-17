package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import java.util.Objects;

public class VariantEntityBehavior {

    // Helper method to ensure currency consistency
    private static void ensureSameCurrency(PriceVO currentPrice, PriceVO newPrice) {
        if (!currentPrice.currency().equals(newPrice.currency())) {
            throw new IllegalArgumentException("Cannot change price currency on an existing variant.");
        }
    }

    /**
     * Creates a new VariantEntity with a modified base price, automatically updating the current price to match.
     * @param original The original variant entity.
     * @param newBasePrice The new base price VO.
     * @return A new, updated VariantEntity instance.
     */
    public static VariantEntity changeBasePrice(VariantEntity original, PriceVO newBasePrice) {
        Objects.requireNonNull(newBasePrice, "newBasePrice must not be null");
        // Business logic often dictates current price matches base price upon change
        return new VariantEntity(
                original.id(),
                original.sku(),
                newBasePrice,
                newBasePrice,
                original.features(),
                original.careInstructions(),
                original.weight(),
                original.status()
        );
    }

    /**
     * Creates a new VariantEntity with a modified current price.
     * @param original The original variant entity.
     * @param newCurrentPrice The new current price VO.
     * @return A new, updated VariantEntity instance.
     */
    public static VariantEntity changeCurrentPrice(VariantEntity original, PriceVO newCurrentPrice) {
        Objects.requireNonNull(newCurrentPrice, "newCurrentPrice must not be null");
        ensureSameCurrency(original.currentPrice(), newCurrentPrice);
        return new VariantEntity(
                original.id(),
                original.sku(),
                original.basePrice(),
                newCurrentPrice,
                original.features(),
                original.careInstructions(),
                original.weight(),
                original.status()
        );
    }

    // --- Lifecycle/Status Behavior Methods ---

    public static VariantEntity activate(VariantEntity original) {
        if (original.status() == VariantStatusEnums.DISCONTINUED) {
            throw new IllegalStateException("Cannot activate a discontinued variant.");
        }
        return new VariantEntity(
                original.id(),
                original.sku(),
                original.basePrice(),
                original.currentPrice(),
                original.features(),
                original.careInstructions(),
                original.weight(),
                VariantStatusEnums.ACTIVE
        );
    }

    public static VariantEntity deactivate(VariantEntity original) {
        return new VariantEntity(
                original.id(),
                original.sku(),
                original.basePrice(),
                original.currentPrice(),
                original.features(),
                original.careInstructions(),
                original.weight(),
                VariantStatusEnums.INACTIVE
        );
    }

    public static VariantEntity markAsDiscontinued(VariantEntity original) {
        return new VariantEntity(
                original.id(),
                original.sku(),
                original.basePrice(),
                original.currentPrice(),
                original.features(),
                original.careInstructions(),
                original.weight(),
                VariantStatusEnums.DISCONTINUED
        );
    }
}

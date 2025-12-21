package com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.VariantDTO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantEntity;

import java.util.stream.Collectors;

/**
 * 2025 Mapper for Variant entities.
 * Delegates feature mapping to FeatureMapper to ensure consistency across the aggregate.
 */
public final class VariantMapper {

    private VariantMapper() {}

    /**
     * Maps VariantEntity (Domain) to VariantDTO (Application).
     * Used when sending product details to the UI/API.
     */
    public static VariantDTO toDTO(VariantEntity variant) {
        if (variant == null) return null;

        return new VariantDTO(
                variant.id().value(),
                variant.sku().sku(),
                variant.basePrice().value(),
                variant.currentPrice().value(),
                variant.basePrice().currency().getCurrencyCode(),
                // Delegate to FeatureMapper for polymorphic feature transformation
                variant.getFeatures().stream()
                        .map(FeatureMapper::toDTO)
                        .collect(Collectors.toSet()),
                variant.careInstructions().instructions(),
                variant.weight().amount(),
                variant.weight().unit().name(),
                variant.status().name()
        );
    }
}

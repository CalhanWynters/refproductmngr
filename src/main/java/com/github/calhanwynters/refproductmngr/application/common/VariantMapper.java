package com.github.calhanwynters.refproductmngr.application.common;

import com.github.calhanwynters.refproductmngr.domain.product.*;
import java.util.stream.Collectors;

import com.github.calhanwynters.refproductmngr.domain.product.*;

public final class VariantMapper {

    private VariantMapper() {}

    /**
     * Maps VariantEntity to VariantDTO for the Application Layer.
     */
    public static VariantDTO toDTO(VariantEntity variant) {
        if (variant == null) return null;

        return new VariantDTO(
                variant.id().value(),
                variant.sku().sku(),
                variant.basePrice().value(),
                variant.currentPrice().value(),
                variant.basePrice().currency().getCurrencyCode(),
                variant.features().stream()
                        .map(VariantMapper::mapFeatureToDTO)
                        .collect(Collectors.toSet()),
                variant.careInstructions().instructions(),
                variant.weight().amount(), // Accessing .amount() from WeightVO record
                variant.weight().unit().name(), // e.g., "GRAM" or "KILOGRAM"
                variant.status().name()
        );
    }

    /**
     * Internal polymorphic mapping using Java 2025 pattern matching.
     */
    private static FeatureDTO mapFeatureToDTO(FeatureAbstractClass feature) {
        String desc = feature.getDescription() != null ? feature.getDescription().description() : null;

        return switch (feature) {
            case FeatureBasicEntity f ->
                    new BasicFeatureDTO(f.getId().value(), f.getNameVO().value(), desc, f.getLabelVO().value());

            case FeatureFixedPriceEntity f ->
                    new FixedPriceFeatureDTO(f.getId().value(), f.getNameVO().value(), desc, f.getLabelVO().value(), f.getFixedPrice());

            case FeatureScalingPriceEntity f ->
                    new ScalingPriceFeatureDTO(f.getId().value(), f.getNameVO().value(), desc, f.getLabelVO().value(),
                            f.getMeasurementUnit().unit(), f.getBaseAmount(), f.getIncrementAmount(), f.getMaxQuantity());

            default -> throw new IllegalArgumentException("Unsupported feature type: " + feature.getClass());
        };
    }
}
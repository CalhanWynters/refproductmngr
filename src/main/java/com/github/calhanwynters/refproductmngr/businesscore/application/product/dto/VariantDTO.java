package com.github.calhanwynters.refproductmngr.businesscore.application.product.dto;

import java.math.BigDecimal;
import java.util.Set;

/**
 * The flat contract for a product variant.
 */
public record VariantDTO(
        String id,
        String sku,
        BigDecimal basePrice,
        BigDecimal currentPrice,
        String currencyCode,
        Set<FeatureDTO> features,
        String careInstructions,
        BigDecimal weightValue, // Changed to BigDecimal to preserve WeightVO precision
        String weightUnit,      // e.g., "KILOGRAM"
        String status
) {}
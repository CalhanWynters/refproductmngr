package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.productcreate;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.FeatureDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 2025 Command for Product Creation.
 * Includes initial physical attributes and polymorphic features.
 */
public record ProductCreateCommand(
        String businessId,
        String category,
        String description,
        List<String> imageUrls,

        // Initial Variant Data
        String initialVariantSku,
        BigDecimal initialPrice,
        String currencyCode,

        // Physical Attributes for Initial Variant
        BigDecimal weightValue,
        String weightUnit,      // e.g., "KILOGRAM", "GRAM"

        // Initial Features (Polymorphic)
        Set<FeatureDTO> features
) {}

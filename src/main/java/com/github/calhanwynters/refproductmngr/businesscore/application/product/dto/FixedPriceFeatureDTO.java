package com.github.calhanwynters.refproductmngr.businesscore.application.product.dto;

import java.math.BigDecimal;

public record FixedPriceFeatureDTO(
        String id,
        String name,
        String label,
        String description,
        boolean isUnique,
        BigDecimal fixedPrice
) implements FeatureDTO {}
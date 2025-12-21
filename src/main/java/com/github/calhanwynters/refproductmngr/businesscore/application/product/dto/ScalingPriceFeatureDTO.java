package com.github.calhanwynters.refproductmngr.businesscore.application.product.dto;

import java.math.BigDecimal;

public record ScalingPriceFeatureDTO(
        String id,
        String name,
        String label,
        String description,
        boolean isUnique,
        String unit,            // Maps to MeasurementUnitVO
        BigDecimal baseAmount,
        BigDecimal incrementAmount,
        int maxQuantity
) implements FeatureDTO {}
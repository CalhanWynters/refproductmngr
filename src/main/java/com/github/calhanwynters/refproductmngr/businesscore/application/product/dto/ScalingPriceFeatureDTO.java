package com.github.calhanwynters.refproductmngr.businesscore.application.product.dto;

import java.math.BigDecimal;

public record ScalingPriceFeatureDTO(
        String id, String name, String description, String label,
        String measurementUnit, BigDecimal baseAmount, BigDecimal incrementAmount, int maxQuantity
) implements FeatureDTO {}
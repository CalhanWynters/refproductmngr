package com.github.calhanwynters.refproductmngr.businesscore.application.product.dto;

public record BasicFeatureDTO(
        String id,
        String name,
        String label,
        String description,
        boolean isUnique
) implements FeatureDTO {}
package com.github.calhanwynters.refproductmngr.businesscore.application.product.dto;

public sealed interface FeatureDTO
        permits BasicFeatureDTO, FixedPriceFeatureDTO, ScalingPriceFeatureDTO {

    String id();          // Nullable during creation, present during updates
    String name();        // Maps to NameVO
    String label();       // Maps to LabelVO
    String description(); // Maps to DescriptionVO (Nullable)
    boolean isUnique();   // New 2025 invariant for single-variant enforcement
}
package com.github.calhanwynters.refproductmngr.application.common;

public sealed interface FeatureDTO
        permits BasicFeatureDTO, FixedPriceFeatureDTO, ScalingPriceFeatureDTO {
    String id();
    String name();
    String label();
    String description(); // Required for the mapper's polymorphic call
}
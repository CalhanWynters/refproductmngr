package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;
import java.util.UUID;

public record FeatureIdVO(String value) {
    /**
     * Compact Constructor for validation. Records provide a concise way to validate fields.
     */
    public FeatureIdVO {
        Objects.requireNonNull(value, "FeatureId value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("FeatureId value cannot be empty or blank");
        }
    }

    /**
     * Static factory method to generate a new, unique FeatureIdVO.
     */
    public static FeatureIdVO generate() {
        // Corrected to return FeatureIdVO instead of VariantIdVO
        return new FeatureIdVO(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}

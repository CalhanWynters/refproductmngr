package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;
import java.util.UUID;

/**
 * Domain value object representing the unique identifier for a Variant Entity.
 */
public record VariantIdVO(String value) {
    public VariantIdVO {
        Objects.requireNonNull(value, "VariantId value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("VariantId value cannot be empty or blank");
        }
    }

    public static VariantIdVO generate() {
        return new VariantIdVO(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
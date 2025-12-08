package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;

/**
 * A Value Object representing the name of a product, feature, or other domain concept.
 * Enforces non-null and non-blank constraints.
 */
public record NameVO(String value) {

    /**
     * Compact constructor for validation.
     */
    public NameVO {
        Objects.requireNonNull(value, "Name value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Name value cannot be empty or blank");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}

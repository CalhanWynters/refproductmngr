package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;

/**
 * A Value Object representing a short, user-facing label (e.g., "Color", "Size").
 * Enforces non-null and non-blank constraints.
 */
public record LabelVO(String value) {

    /**
     * Compact constructor for validation, ensuring the label is never null or empty/blank.
     */
    public LabelVO {
        Objects.requireNonNull(value, "Label value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Label value cannot be empty or blank");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}

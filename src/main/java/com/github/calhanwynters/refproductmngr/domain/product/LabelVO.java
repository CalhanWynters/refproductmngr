package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;

/**
 * A Value Object representing a short, user-facing label (e.g., "Color", "Size").
 * Enforces non-null, non-blank, and length constraints.
 */
public record LabelVO(String value) {

    /**
     * Compact constructor for validation, ensuring the label is never null, empty, or too long.
     */
    public LabelVO {
        Objects.requireNonNull(value, "Label value cannot be null");

        // Normalize the value by trimming it
        String normalizedValue = value.trim();

        if (normalizedValue.isBlank()) {
            throw new IllegalArgumentException("Label value cannot be empty or blank");
        }

        if (normalizedValue.length() > 100) { // Example length limit
            throw new IllegalArgumentException("Label value cannot exceed 100 characters.");
        }

        // Always super call is not necessary, value is already set during declaration
        // Just validate using the validated normalizedValue, we don't need to change this.value
    }

    @Override
    public String toString() {
        return value; // This is accessible as-is
    }
}

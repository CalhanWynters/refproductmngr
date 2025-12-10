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

        // Normalize input
        String trimmedValue = value.trim();

        if (trimmedValue.isEmpty()) {
            throw new IllegalArgumentException("Name value cannot be empty or blank");
        }

        // Optional: Length constraint
        if (trimmedValue.length() > 100) { // Adjust max length as needed
            throw new IllegalArgumentException("Name value cannot exceed 100 characters.");
        }

        // Note that you do NOT need to assign trimmedValue to this.value
        // The record constructor automatically sets this.value to the provided parameter.
    }

    @Override
    public String toString() {
        return value; // Returns the unit as-is
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameVO nameVO = (NameVO) o;
        return Objects.equals(value, nameVO.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

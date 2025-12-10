package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;
import java.util.UUID;

/**
 * Domain value object representing the unique identifier for a Variant Entity.
 */
public record VariantIdVO(String value) {

    public VariantIdVO {
        Objects.requireNonNull(value, "VariantId value cannot be null");

        // Normalize input by trimming whitespace
        String trimmedValue = value.trim();

        if (trimmedValue.isEmpty()) {
            throw new IllegalArgumentException("VariantId value cannot be empty or blank");
        }

        // Optional length validation
        if (trimmedValue.length() > 100) {
            throw new IllegalArgumentException("VariantId cannot exceed 100 characters.");
        }
    }

    /**
     * Static factory method to generate a new, unique VariantIdVO using a random UUID.
     * @return A new instance of VariantIdVO.
     */
    public static VariantIdVO generate() {
        return new VariantIdVO(UUID.randomUUID().toString());
    }

    /**
     * Static factory method to create a VariantIdVO from a given string.
     * Validates that the input string is not null or blank.
     * @param id the string representation of the VariantId
     * @return A new instance of VariantIdVO
     */
    public static VariantIdVO fromString(String id) {
        return new VariantIdVO(id);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariantIdVO(String value1))) return false;
        return Objects.equals(value, value1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

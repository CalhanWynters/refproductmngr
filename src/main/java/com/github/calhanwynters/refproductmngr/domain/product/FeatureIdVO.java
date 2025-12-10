package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a unique identifier for a feature in the system.
 */
public record FeatureIdVO(String value) {

    /**
     * Compact constructor for validation.
     * Ensures that the value is not null or blank.
     */
    public FeatureIdVO {
        Objects.requireNonNull(value, "FeatureId value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("FeatureId value cannot be empty or blank");
        }
        // Optional length validation
        if (value.length() > 100) { // Example maximum length
            throw new IllegalArgumentException("FeatureId cannot exceed 100 characters.");
        }
    }

    /**
     * Static factory method to generate a new, unique FeatureIdVO using a random UUID.
     * @return A new instance of FeatureIdVO.
     */
    public static FeatureIdVO generate() {
        return new FeatureIdVO(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeatureIdVO(String value1))) return false;
        return Objects.equals(value, value1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

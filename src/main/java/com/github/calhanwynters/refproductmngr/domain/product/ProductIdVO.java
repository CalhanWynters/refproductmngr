package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;
import java.util.UUID;

/**
 * Domain value object representing the unique identifier for a Product aggregate.
 */
public record ProductIdVO(String value) {

    /**
     * Compact constructor for validation.
     * Ensures that the value is not null or blank.
     */
    public ProductIdVO {
        Objects.requireNonNull(value, "ProductId value cannot be null");
        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty()) {
            throw new IllegalArgumentException("ProductId value cannot be empty or blank");
        }
        // Optional length validation if needed
        if (trimmedValue.length() > 100) { // Example maximum length
            throw new IllegalArgumentException("ProductId cannot exceed 100 characters.");
        }
    }

    /**
     * Static factory method to generate a new, unique ProductIdVO using a random UUID.
     * @return A new instance of ProductIdVO.
     */
    public static ProductIdVO generate() {
        return new ProductIdVO(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductIdVO(String value1))) return false; // Pattern matching in Java 16+
        return Objects.equals(value, value1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;

/**
 * Value Object representing a Stock Keeping Unit (SKU).
 */
public record SkuVO(String sku) {

    public SkuVO {
        // Use requireNonNull for null check
        Objects.requireNonNull(sku, "SKU cannot be null");

        // Normalize the SKU by trimming whitespace
        String trimmedSku = sku.trim();

        // Check that the trimmed SKU is not empty
        if (trimmedSku.isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be empty");
        }

        // This implicit assignment happens automatically
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkuVO(String sku1))) return false; // Pattern matching in Java 16+
        return Objects.equals(sku, sku1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku); // Provide a hash code for consistency in collections
    }

    @Override
    public String toString() {
        return sku; // Return SKU as a string
    }
}

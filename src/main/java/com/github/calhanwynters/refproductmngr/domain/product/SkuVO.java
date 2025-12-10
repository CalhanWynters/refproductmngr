package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing a Stock Keeping Unit (SKU).
 * Enforces non-null, non-empty, length, and strict content constraints using whitelisting.
 */
public record SkuVO(String sku) {

    // Whitelist pattern: Allows letters (upper/lower), numbers, hyphens, and underscores.
    private static final Pattern ALLOWED_CHARS_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+");
    private static final int MAX_LENGTH = 50; // Common max length for SKUs

    public SkuVO {
        Objects.requireNonNull(sku, "SKU cannot be null");

        // Normalize the SKU by trimming whitespace
        String trimmedSku = sku.trim();

        // Check that the trimmed SKU is not empty
        if (trimmedSku.isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be empty");
        }

        if (trimmedSku.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("SKU cannot exceed " + MAX_LENGTH + " characters.");
        }

        // --- Cybersecurity Enhancement: Whitelisting ---
        if (!ALLOWED_CHARS_PATTERN.matcher(trimmedSku).matches()) {
            throw new IllegalArgumentException("SKU contains forbidden characters. Only letters, numbers, hyphens, and underscores are allowed.");
        }

        // Store the normalized value
        sku = trimmedSku;
    }
}

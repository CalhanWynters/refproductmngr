package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A Value Object representing a short, user-facing label (e.g., "Color", "Size").
 * Enforces non-null, non-blank, length, and strict content constraints.
 */
public record LabelVO(String value) {

    // Whitelist pattern: Allows letters (upper/lower), numbers, spaces, and hyphens.
    private static final Pattern ALLOWED_CHARS_PATTERN = Pattern.compile("[a-zA-Z0-9 -]+");
    private static final int MAX_LENGTH = 100;

    /**
     * Compact constructor for validation, ensuring the label is never null, empty, or too long,
     * and contains only allowed characters.
     */
    public LabelVO {
        Objects.requireNonNull(value, "Label value cannot be null");

        // Normalize the value by trimming it
        String normalizedValue = value.trim();

        if (normalizedValue.isBlank()) {
            throw new IllegalArgumentException("Label value cannot be empty or blank");
        }

        if (normalizedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Label value cannot exceed " + MAX_LENGTH + " characters.");
        }

        // --- Cybersecurity Enhancement: Whitelisting ---
        // Ensures only a specific set of safe characters are allowed, blocking injections/XSS
        if (!ALLOWED_CHARS_PATTERN.matcher(normalizedValue).matches()) {
            throw new IllegalArgumentException("Label contains forbidden characters. Only letters, numbers, spaces, and hyphens are allowed.");
        }
        // ----------------------------------------------

        // The record's internal 'value' component will store the normalized value
        value = normalizedValue;
    }

    // Records automatically provide toString, equals, hashCode, and the accessor method value().
    // The custom toString provided in the original request is removed as it's redundant.
}

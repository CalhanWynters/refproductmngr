package com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Represents a unique identifier for a feature in the system, stored as a String internally
 * for validation flexibility but representing a UUID structure.
 */
public record FeatureIdVO(String value) {

    // Regex pattern for a standard UUID (case-insensitive)
    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    /**
     * Compact constructor for validation.
     * Ensures that the value is not null, non-blank, and is a valid UUID format string.
     */
    public FeatureIdVO {
        Objects.requireNonNull(value, "FeatureId value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("FeatureId value cannot be empty or blank");
        }

        // Validate the input string against a known safe format (UUID regex)
        if (!UUID_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("FeatureId must be a valid UUID format.");
        }
    }

    /**
     * Static factory method to generate a new, unique FeatureIdVO using a random UUID.
     * @return A new instance of FeatureIdVO.
     */
    public static FeatureIdVO generate() {
        return new FeatureIdVO(UUID.randomUUID().toString());
    }

    /**
     * Static factory method to create a FeatureIdVO from an existing value (e.g., from a database).
     * @param value The existing ID string.
     * @return A new instance of FeatureIdVO if valid.
     */
    public static FeatureIdVO fromString(String value) {
        return new FeatureIdVO(value);
    }

    /**
     * Helper method to return the underlying standard UUID object if needed for external systems.
     * @return The UUID object.
     */
    public UUID toUUID() {
        return UUID.fromString(this.value);
    }
}

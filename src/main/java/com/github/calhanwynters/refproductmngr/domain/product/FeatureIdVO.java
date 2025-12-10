package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Represents a unique identifier for a feature in the system.
 */
public record FeatureIdVO(String value) {

    // Regex pattern for a standard UUID (case-insensitive)
    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private static final int MAX_LENGTH = 100;

    /**
     * Compact constructor for validation.
     * Ensures that the value is not null, blank, meets length constraints, and is a valid UUID format.
     */
    public FeatureIdVO {
        Objects.requireNonNull(value, "FeatureId value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("FeatureId value cannot be empty or blank");
        }

        // Ensure the ID adheres to a specific business ID length constraint (optional)
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("FeatureId cannot exceed " + MAX_LENGTH + " characters.");
        }

        // Validate the input string against a known safe format (UUID)
        if (!UUID_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("FeatureId must be a valid UUID format.");
        }
    }

    /**
     * Static factory method to generate a new, unique FeatureIdVO using a random UUID.
     * @return A new instance of FeatureIdVO.
     */
    public static FeatureIdVO generate() {
        // The generated UUID string will pass the validation regex automatically.
        return new FeatureIdVO(UUID.randomUUID().toString());
    }

    /**
     * Static factory method to create a FeatureIdVO from an existing value (e.g., from a database).
     * @param value The existing ID string.
     * @return A new instance of FeatureIdVO if valid.
     */
    public static FeatureIdVO fromString(String value) {
        // The constructor handles all validation for the input string.
        return new FeatureIdVO(value);
    }

    // Default record methods (toString, equals, hashCode) are used automatically.
    // Custom overrides have been removed for cleaner code.
}

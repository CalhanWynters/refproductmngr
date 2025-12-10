package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Domain value object representing the unique identifier for a Variant Entity.
 * Ensures the value conforms to the UUID format using whitelisting/regex validation.
 */
public record VariantIdVO(String value) {

    // Regex pattern for a standard UUID (case-insensitive)
    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private static final int MAX_LENGTH = 100;

    /**
     * Compact constructor for validation.
     * Ensures that the value is not null, blank, and is a valid UUID format.
     */
    public VariantIdVO {
        Objects.requireNonNull(value, "VariantId value cannot be null");
        String trimmedValue = value.trim();

        if (trimmedValue.isEmpty()) {
            throw new IllegalArgumentException("VariantId value cannot be empty or blank");
        }

        if (trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("VariantId cannot exceed " + MAX_LENGTH + " characters.");
        }

        // Validate the input string against the known safe format (UUID)
        if (!UUID_PATTERN.matcher(trimmedValue).matches()) {
            // This prevents arbitrary strings/injection attacks from being treated as valid IDs
            throw new IllegalArgumentException("VariantId must be a valid UUID format.");
        }

        // The record's internal 'value' component will store the trimmed value
        value = trimmedValue;
    }

    /**
     * Static factory method to generate a new, unique VariantIdVO using a random UUID.
     * @return A new instance of VariantIdVO.
     */
    public static VariantIdVO generate() {
        // The generated UUID string will automatically pass the validation regex.
        return new VariantIdVO(UUID.randomUUID().toString());
    }

    /**
     * Static factory method to create a VariantIdVO from a given string.
     * The constructor handles all validation.
     * @param id the string representation of the VariantId
     * @return A new instance of VariantIdVO
     */
    public static VariantIdVO fromString(String id) {
        return new VariantIdVO(id);
    }

    // Default record methods (toString, equals, hashCode) are used automatically.
    // The custom overrides are removed for cleaner code.
}

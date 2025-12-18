package com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant;

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

    // private static final int MAX_LENGTH = 100; // Removed, as UUID format is fixed length (36)

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

        /*
        // Optional: If you keep the MAX_LENGTH check:
        if (trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("VariantId cannot exceed " + MAX_LENGTH + " characters.");
        }
        */

        // Validate the input string against the known safe format (UUID)
        if (!UUID_PATTERN.matcher(trimmedValue).matches()) {
            // This prevents arbitrary strings/injection attacks from being treated as valid IDs
            throw new IllegalArgumentException("VariantId must be a valid UUID format.");
        }

        // The record's internal 'value' component will store the trimmed value
        value = trimmedValue;
    }

    // ... (generate() and fromString() factory methods remain the same) ...
    public static VariantIdVO generate() {
        return new VariantIdVO(UUID.randomUUID().toString());
    }
    public static VariantIdVO fromString(String id) {
        return new VariantIdVO(id);
    }
}

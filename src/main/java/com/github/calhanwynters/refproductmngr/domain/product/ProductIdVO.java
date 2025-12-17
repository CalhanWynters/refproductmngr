package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Domain value object representing the unique identifier for a Product aggregate.
 * Ensures the value conforms to the UUID format using whitelisting/regex validation.
 */
public record ProductIdVO(String value) {

    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    /**
     * Compact constructor for validation.
     * Ensures that the value is not null or blank and is a valid UUID format.
     */
    public ProductIdVO {
        Objects.requireNonNull(value, "ProductId value cannot be null");
        String trimmedValue = value.trim();

        if (trimmedValue.isEmpty()) {
            throw new IllegalArgumentException("ProductId value cannot be empty or blank");
        }

        // Validate the input string against the known safe format (UUID)
        if (!isValidUUID(trimmedValue)) {
            throw new IllegalArgumentException("ProductId must be a valid UUID format.");
        }
    }

    /**
     * Static method to validate if a string is a valid UUID format.
     */
    public static boolean isValidUUID(String uuid) {
        return UUID_PATTERN.matcher(uuid).matches();
    }

    /**
     * Static factory method to generate a new, unique ProductIdVO using a random UUID.
     * @return A new instance of ProductIdVO.
     */
    public static ProductIdVO generate() {
        return new ProductIdVO(UUID.randomUUID().toString());
    }

    // Default record methods (toString, equals, hashCode) are used automatically.
}
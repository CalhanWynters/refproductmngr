package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A Value Object representing a measurement unit (e.g., "kg", "m", "cm").
 * Enforces non-null, non-empty, and strict content constraints using whitelisting.
 */
public record MeasurementUnitVO(String unit) {

    // Whitelist pattern: Allows letters (upper/lower), numbers, and common symbols used in units (.,%,°)
    private static final Pattern ALLOWED_CHARS_PATTERN = Pattern.compile("[a-zA-Z0-9.,%°]+");
    private static final int MAX_LENGTH = 20; // Example: Units are usually short

    /**
     * Compact constructor for validation, ensuring the unit is never null, empty,
     * within length limits, and contains only allowed characters.
     */
    public MeasurementUnitVO {
        Objects.requireNonNull(unit, "Measurement unit must not be null.");

        // Normalize input by trimming
        String trimmedUnit = unit.trim();

        if (trimmedUnit.isEmpty()) {
            throw new IllegalArgumentException("Measurement unit must not be empty.");
        }

        if (trimmedUnit.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Measurement unit cannot exceed " + MAX_LENGTH + " characters.");
        }

        // --- Cybersecurity Enhancement: Whitelisting ---
        if (!ALLOWED_CHARS_PATTERN.matcher(trimmedUnit).matches()) {
            throw new IllegalArgumentException("Measurement unit contains forbidden characters. Only letters, numbers, '.', '%', and '°' are allowed.");
        }
        // ----------------------------------------------

        // The record's internal 'unit' component will store the normalized value
        unit = trimmedUnit;
    }

    // Default record methods (toString, equals, hashCode, and the accessor method unit()) are sufficient.
    // The custom overrides have been removed for cleaner code.
}

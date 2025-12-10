package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a unique identifier for a business.
 */
public record BusinessIdVO(String value) {
    public BusinessIdVO {
        Objects.requireNonNull(value, "BusinessId value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("BusinessId value cannot be empty or blank");
        }
        // Strict format check using whitelisting
        if (!value.matches("[A-Z0-9-]+")) {
            throw new IllegalArgumentException("BusinessId must consist of uppercase letters, numbers, and dashes.");
        }
    }

    /**
     * Static factory method to generate a new BusinessId using a random UUID,
     * normalized to uppercase to match validation rules.
     *
     * @return A new instance of BusinessIdVO.
     */
    public static BusinessIdVO random() {
        // Fix: Convert to uppercase so it passes the regex validation in the constructor
        return new BusinessIdVO(UUID.randomUUID().toString().toUpperCase(Locale.ROOT));
    }

    /**
     * Static factory method to create a BusinessIdVO from a specific value.
     *
     * @param value The specific value for the BusinessId.
     * @return A new BusinessIdVO with the provided value.
     */
    public static BusinessIdVO fromValue(String value) {
        return new BusinessIdVO(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

package com.github.calhanwynters.refproductmngr.domain.product;

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
        // Optional format check, adjust as needed
        if (!value.matches("[A-Z0-9-]+")) {
            throw new IllegalArgumentException("BusinessId must consist of uppercase letters, numbers, and dashes.");
        }
    }

    /**
     * Static factory method to generate a new BusinessId using a random UUID.
     *
     * @return A new instance of BusinessIdVO.
     */
    public static BusinessIdVO random() {
        return new BusinessIdVO(UUID.randomUUID().toString());
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

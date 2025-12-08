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
    }

    /**
     * Generates a new BusinessId using a random UUID.
     * @return A new instance of BusinessId.
     */
    public static BusinessIdVO generate() {

        return new BusinessIdVO(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}

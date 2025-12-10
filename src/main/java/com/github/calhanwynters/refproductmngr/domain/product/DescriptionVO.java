package com.github.calhanwynters.refproductmngr.domain.product;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Domain value object for product descriptions.
 * Encapsulates validation and normalization rules for a description string.
 */
public record DescriptionVO(String description) {
    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 2000;

    // Constructor with validation and normalization
    public DescriptionVO {
        Objects.requireNonNull(description, "Description value cannot be null");
        String stripped = description.strip();
        String normalized = getString(stripped);

        String[] forbiddenWords = {"forbiddenWord1", "forbiddenWord2"};

        for (String forbidden : forbiddenWords) {
            if (description.toLowerCase().contains(forbidden)) {
                throw new IllegalArgumentException("Instructions contain forbidden words.");
            }
        }

        // Set the internal description to the normalized description
        description = normalized;
    }

    private static @NonNull String getString(String stripped) {
        String normalized = stripped.replaceAll("\\s+", " "); // Normalize white space

        if (normalized.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Description must be at least " + MIN_LENGTH + " characters long.");
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Description cannot exceed " + MAX_LENGTH + " characters.");
        }

        if (normalized.contains("<") || normalized.contains(">")) {
            throw new IllegalArgumentException("Description must not contain HTML tags.");
        }
        return normalized;
    }

    // Example domain behavior
    public DescriptionVO truncate(int maxLength) {
        if (description.length() <= maxLength) {
            return this;
        }
        // Return a new description object with the truncated string
        String truncated = description.substring(0, maxLength - 3) + "...";
        return new DescriptionVO(truncated);
    }

    // Override toString
    @Override
    public String toString() {
        return "DescriptionVO{" + "description='" + description + '\'' + '}';
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DescriptionVO(String description1))) return false;
        return Objects.equals(description, description1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}

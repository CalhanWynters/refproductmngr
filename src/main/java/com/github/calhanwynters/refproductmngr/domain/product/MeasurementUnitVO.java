package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;

/**
 * A Value Object representing a measurement unit (e.g., "kg", "m", "cm").
 * Enforces non-null and non-empty constraints.
 */
public record MeasurementUnitVO(String unit) {

    /**
     * Compact constructor for validation, ensuring the unit is never null or empty.
     */
    public MeasurementUnitVO {
        // Check for null and empty input
        Objects.requireNonNull(unit, "Measurement unit must not be null.");

        // Normalize input by trimming and validating
        String trimmedUnit = unit.trim();

        if (trimmedUnit.isEmpty()) {
            throw new IllegalArgumentException("Measurement unit must not be empty.");
        }

        // Do not try to assign to 'this.unit'; it is already assigned when the record is created
    }

    @Override
    public String toString() {
        return unit; // Returns the unit as-is
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementUnitVO that = (MeasurementUnitVO) o;
        return Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit); // Ensure consistent hash code
    }
}

package com.github.calhanwynters.refproductmngr.domain.product;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * A Value Object (VO) enum for handling different weight units and their conversions.
 * It uses BigDecimal for precise arithmetic, suitable for financial or inventory systems.
 */
public enum WeightUnitEnums {
    /** Represents the base unit of a Gram. */
    GRAM {
        @Override
        public BigDecimal toGrams(BigDecimal v) {
            validateInput(v);
            return v;
        }

        @Override
        public BigDecimal fromGrams(BigDecimal g) {
            validateInput(g);
            return g;
        }
    },
    /** Represents the unit of a Kilogram (SI unit of mass). */
    KILOGRAM {
        @Override
        public BigDecimal toGrams(BigDecimal v) {
            validateInput(v);
            return v.multiply(GRAMS_PER_KILOGRAM, MC);
        }

        @Override
        public BigDecimal fromGrams(BigDecimal g) {
            validateInput(g);
            return g.divide(GRAMS_PER_KILOGRAM, SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    },
    /** Represents the unit of an Avoirdupois Ounce (general purpose ounce). */
    OUNCE {
        @Override
        public BigDecimal toGrams(BigDecimal v) {
            validateInput(v);
            return v.multiply(GRAMS_PER_OUNCE, MC);
        }

        @Override
        public BigDecimal fromGrams(BigDecimal g) {
            validateInput(g);
            return g.divide(GRAMS_PER_OUNCE, SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    },
    /** Represents the unit of an Avoirdupois Pound (general purpose pound). */
    POUND {
        @Override
        public BigDecimal toGrams(BigDecimal v) {
            validateInput(v);
            return v.multiply(GRAMS_PER_POUND, MC);
        }

        @Override
        public BigDecimal fromGrams(BigDecimal g) {
            validateInput(g);
            return g.divide(GRAMS_PER_POUND, SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    },
    /** Represents the unit of a Carat (used for gemstones). */
    CARAT {
        @Override
        public BigDecimal toGrams(BigDecimal v) {
            validateInput(v);
            return v.multiply(GRAMS_PER_CARAT, MC);
        }

        @Override
        public BigDecimal fromGrams(BigDecimal g) {
            validateInput(g);
            return g.divide(GRAMS_PER_CARAT, SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    },
    /** Represents the unit of a Troy Ounce (used for precious metals). */
    TROY_OUNCE {
        @Override
        public BigDecimal toGrams(BigDecimal v) {
            validateInput(v);
            return v.multiply(GRAMS_PER_TROY_OUNCE, MC);
        }

        @Override
        public BigDecimal fromGrams(BigDecimal g) {
            validateInput(g);
            return g.divide(GRAMS_PER_TROY_OUNCE, SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    };

    // Constants for conversion factors
    private static final BigDecimal GRAMS_PER_KILOGRAM = BigDecimal.valueOf(1000.0);
    private static final BigDecimal GRAMS_PER_POUND = BigDecimal.valueOf(453.59237);
    private static final BigDecimal GRAMS_PER_OUNCE = BigDecimal.valueOf(28.349523125);
    private static final BigDecimal GRAMS_PER_CARAT = BigDecimal.valueOf(0.2);
    private static final BigDecimal GRAMS_PER_TROY_OUNCE = BigDecimal.valueOf(31.1034768); // Standard for precious metals

    // Scale and MathContext for precision
    private static final int SCALE = 8; // Preserves sub-milligram precision (0.00000001 g)
    private static final MathContext MC = new MathContext(16, RoundingMode.HALF_UP);
    private static final BigDecimal MAX_ALLOWABLE_WEIGHT = BigDecimal.valueOf(1000000.0); // Max weight limit

    /**
     * Validates the input value for null, negative, or zero weight.
     * @param value The value to validate.
     * @throws IllegalArgumentException if the value is null, negative, zero, or exceeds max weight.
     */
    private static void validateInput(BigDecimal value) {
        Objects.requireNonNull(value, "Value must not be null");
        if (value.signum() <= 0) {
            throw new IllegalArgumentException("Value must be positive and non-zero; given: " + value);
        }
        if (value.compareTo(MAX_ALLOWABLE_WEIGHT) > 0) {
            throw new IllegalArgumentException("Value exceeds the maximum allowable weight of " + MAX_ALLOWABLE_WEIGHT + " grams; given: " + value);
        }
    }

    /**
     * Converts a value in this unit to grams.
     * @param value The value in the current unit. Must be positive and non-zero.
     * @return The value in grams.
     */
    public abstract BigDecimal toGrams(BigDecimal value);

    /**
     * Converts a value in grams to this unit.
     * @param grams The value in grams. Must be positive and non-zero.
     * @return The value in the current unit, rounded to the defined SCALE.
     */
    public abstract BigDecimal fromGrams(BigDecimal grams);

    /**
     * Converts a given value from the current unit to a specified target unit.
     * @param value The value in the current unit (this). Must be positive and non-zero.
     * @param targetUnit The desired unit for the result. Must not be null.
     * @return The converted value in the target unit.
     */
    public BigDecimal convertValueTo(BigDecimal value, WeightUnitEnums targetUnit) {
        validateInput(value);
        Objects.requireNonNull(targetUnit, "Target unit must not be null");
        if (this == targetUnit) {
            // No conversion needed; return validated value directly.
            return value.stripTrailingZeros();
        }

        // Convert to intermediate grams using high precision internally.
        BigDecimal grams = this.toGrams(value);
        // Convert from grams to the target unit.
        return targetUnit.fromGrams(grams);
    }
}


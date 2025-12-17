package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * A Value Object (VO) enum for handling different weight units and their conversions.
 * It uses BigDecimal for precise arithmetic.
 */
public enum WeightUnitEnums {
    // ... (Enum constants like GRAM, KILOGRAM, OUNCE remain the same structure) ...
    GRAM {
        @Override
        public BigDecimal toGrams(BigDecimal v) { return v; }
        @Override
        public BigDecimal fromGrams(BigDecimal g) { return g; }
    },
    KILOGRAM {
        @Override
        public BigDecimal toGrams(BigDecimal v) {
            return v.multiply(GRAMS_PER_KILOGRAM, WeightConstants.INTERNAL_MATH_CONTEXT);
        }
        @Override
        public BigDecimal fromGrams(BigDecimal g) {
            return g.divide(GRAMS_PER_KILOGRAM, WeightConstants.INTERNAL_CALCULATION_SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    },
    // ... (POUND, OUNCE, CARAT, TROY_OUNCE implementations follow the same pattern) ...
    POUND {
        @Override
        public BigDecimal toGrams(BigDecimal v) {
            return v.multiply(GRAMS_PER_POUND, WeightConstants.INTERNAL_MATH_CONTEXT);
        }

        @Override
        public BigDecimal fromGrams(BigDecimal g) {
            return g.divide(GRAMS_PER_POUND, WeightConstants.INTERNAL_CALCULATION_SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    },
    OUNCE {
        @Override
        public BigDecimal toGrams(BigDecimal v) {
            return v.multiply(GRAMS_PER_OUNCE, WeightConstants.INTERNAL_MATH_CONTEXT);
        }

        @Override
        public BigDecimal fromGrams(BigDecimal g) {
            return g.divide(GRAMS_PER_OUNCE, WeightConstants.INTERNAL_CALCULATION_SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    },
    CARAT {
        @Override
        public BigDecimal toGrams(BigDecimal v) {
            return v.multiply(GRAMS_PER_CARAT, WeightConstants.INTERNAL_MATH_CONTEXT);
        }

        @Override
        public BigDecimal fromGrams(BigDecimal g) {
            return g.divide(GRAMS_PER_CARAT, WeightConstants.INTERNAL_CALCULATION_SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    },
    TROY_OUNCE {
        @Override
        public BigDecimal toGrams(BigDecimal v) {
            return v.multiply(GRAMS_PER_TROY_OUNCE, WeightConstants.INTERNAL_MATH_CONTEXT);
        }

        @Override
        public BigDecimal fromGrams(BigDecimal g) {
            return g.divide(GRAMS_PER_TROY_OUNCE, WeightConstants.INTERNAL_CALCULATION_SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    };

    // Constants for conversion factors (moved from the original enum body)
    private static final BigDecimal GRAMS_PER_KILOGRAM = BigDecimal.valueOf(1000.0);
    private static final BigDecimal GRAMS_PER_POUND = BigDecimal.valueOf(453.59237);
    private static final BigDecimal GRAMS_PER_OUNCE = BigDecimal.valueOf(28.349523125);
    private static final BigDecimal GRAMS_PER_CARAT = BigDecimal.valueOf(0.2);
    private static final BigDecimal GRAMS_PER_TROY_OUNCE = BigDecimal.valueOf(31.1034768);

    // Removed the private validateInput() method, as validation belongs in WeightVO

    /**
     * Converts a value in this unit to grams.
     * Assumes input value is non-null and positive (validated by WeightVO).
     * @param value The value in the current unit.
     * @return The value in grams.
     */
    public abstract BigDecimal toGrams(BigDecimal value);

    /**
     * Converts a value in grams to this unit.
     * Assumes input value is non-null and positive (validated by WeightVO).
     * @param grams The value in grams.
     * @return The value in the current unit, rounded and stripped of trailing zeros.
     */
    public abstract BigDecimal fromGrams(BigDecimal grams);

    /**
     * Converts a given value from the current unit to a specified target unit.
     * @param value The value in the current unit (this).
     * @param targetUnit The desired unit for the result. Must not be null.
     * @return The converted value in the target unit.
     */
    public BigDecimal convertValueTo(BigDecimal value, WeightUnitEnums targetUnit) {
        Objects.requireNonNull(targetUnit, "Target unit must not be null");

        // Validation of 'value' is assumed to be handled by the WeightVO compact constructor.

        if (this == targetUnit) {
            return value.stripTrailingZeros();
        }

        // Convert to intermediate grams using high precision internally.
        BigDecimal grams = this.toGrams(value);
        // Convert from grams to the target unit.
        return targetUnit.fromGrams(grams);
    }
}

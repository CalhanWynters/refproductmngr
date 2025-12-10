package com.github.calhanwynters.refproductmngr.domain.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Domain value object representing a product weight.* This record ensures immutability, validation, and standard weight operations.*/
public record WeightVO(BigDecimal amount, WeightUnitEnums unit) implements Comparable<WeightVO> {

    // Constructor for validation and normalization
    public WeightVO {
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(unit, "Unit must not be null");

        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }

        // Normalize the input amount before storing in the record using the standard scale
        amount = normalize(amount);

        // Validate maximum weight in grams using the shared constant
        if (unit.toGrams(amount).compareTo(WeightConstants.MAX_GRAMS) > 0) {
            throw new IllegalArgumentException(
                    "Amount exceeds maximum allowed weight (" + WeightConstants.MAX_GRAMS.stripTrailingZeros().toPlainString() + "g)"
            );
        }

        // Validate minimum weight using the shared constant
        BigDecimal weightInGrams = unit.toGrams(amount);
        // We allow 0 weight, but if positive, it must meet the minimum threshold.
        if (weightInGrams.compareTo(BigDecimal.ZERO) > 0 && weightInGrams.compareTo(WeightConstants.MIN_GRAMS) < 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than " + WeightConstants.MIN_GRAMS.stripTrailingZeros().toPlainString() + "g"
            );
        }
    }

    /*** Helper method to standardize BigDecimal formatting for this VO.*/
    private static BigDecimal normalize(BigDecimal value) {
        // Standardize the scale for the stored value in the record
        return value.setScale(WeightConstants.NORMALIZATION_SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    // --- Factory methods (These are what you need to ensure are present) ---

    public static WeightVO ofGrams(BigDecimal grams) {
        return new WeightVO(grams, WeightUnitEnums.GRAM);
    }

    public static WeightVO ofKilograms(BigDecimal kilograms) {
        return new WeightVO(kilograms, WeightUnitEnums.KILOGRAM);
    }

    public static WeightVO ofPounds(BigDecimal pounds) {
        return new WeightVO(pounds, WeightUnitEnums.POUND);
    }

    public static WeightVO ofOunces(BigDecimal ounces) {
        return new WeightVO(ounces, WeightUnitEnums.OUNCE);
    }

    public static WeightVO ofTroyOunces(BigDecimal troyOunces) {
        return new WeightVO(troyOunces, WeightUnitEnums.TROY_OUNCE);
    }

    public static WeightVO ofCarats(BigDecimal carats) {
        // Note: Added this for completeness, previously missing from the original list
        return new WeightVO(carats, WeightUnitEnums.CARAT);
    }


    /**
     * Compares this WeightVO to another based on their value in grams.
     * This provides a natural ordering consistent with equals().
     */
    @Override
    public int compareTo(WeightVO other) {
        // Convert both weights to a common base unit (grams) for accurate comparison
        BigDecimal thisGrams = this.unit.toGrams(this.amount)
                .setScale(WeightConstants.COMPARISON_SCALE, RoundingMode.HALF_UP);
        BigDecimal otherGrams = other.unit.toGrams(other.amount)
                .setScale(WeightConstants.COMPARISON_SCALE, RoundingMode.HALF_UP);
        return thisGrams.compareTo(otherGrams);
    }

    /**
     * Converts the current WeightVO instance into a new WeightVO instance
     * represented in a different unit.
     * @param targetUnit The desired unit for the new WeightVO.
     * @return A new WeightVO in the target unit.
     */
    public WeightVO convertTo(WeightUnitEnums targetUnit) {
        BigDecimal convertedAmount = this.unit.convertValueTo(this.amount, targetUnit);
        return new WeightVO(convertedAmount, targetUnit);
    }
}

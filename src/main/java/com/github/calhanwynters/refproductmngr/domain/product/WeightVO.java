package com.github.calhanwynters.refproductmngr.domain.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Domain value object representing a product weight.
 * This record ensures immutability, validation, and standard weight operations.
 */
public record WeightVO(BigDecimal amount, WeightUnitEnums unit) implements Comparable<WeightVO> {

    // Centralized constants for weight limits and normalization scale
    private static final BigDecimal MAX_GRAMS = BigDecimal.valueOf(100000.0); // 100 kg
    private static final BigDecimal MIN_GRAMS = BigDecimal.valueOf(0.001); // 1 milligram
    private static final int COMPARISON_SCALE = 8; // Scale used for internal comparison in grams
    private static final int NORMALIZATION_SCALE = 4; // Scale for normalization

    // Constructor for validation and normalization
    public WeightVO {
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(unit, "Unit must not be null");

        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }

        // Normalize the input amount before storing in the record
        amount = normalize(amount);

        // Validate maximum weight in grams
        if (unit.toGrams(amount).compareTo(MAX_GRAMS) > 0) {
            throw new IllegalArgumentException("Amount exceeds maximum allowed weight (" + MAX_GRAMS.stripTrailingZeros().toPlainString() + "g)");
        }

        // Validate minimum weight
        BigDecimal weightInGrams = unit.toGrams(amount);
        if (weightInGrams.compareTo(BigDecimal.ZERO) > 0 && weightInGrams.compareTo(MIN_GRAMS) < 0) {
            throw new IllegalArgumentException("Amount must be greater than " + MIN_GRAMS.stripTrailingZeros().toPlainString() + "g");
        }
    }

    /**
     * Helper method to standardize BigDecimal formatting for this VO.
     */
    private static BigDecimal normalize(BigDecimal value) {
        // Standardize the scale for the stored value in the record
        return value.setScale(NORMALIZATION_SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    // Factory methods
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

    /**
     * Compares this WeightVO to another based on their value in grams.
     * This provides a natural ordering consistent with equals().
     */
    @Override
    public int compareTo(WeightVO other) {
        // Convert both weights to a common base unit (grams) for accurate comparison
        BigDecimal thisGrams = this.unit.toGrams(this.amount).setScale(COMPARISON_SCALE, RoundingMode.HALF_UP);
        BigDecimal otherGrams = other.unit.toGrams(other.amount).setScale(COMPARISON_SCALE, RoundingMode.HALF_UP);

        return thisGrams.compareTo(otherGrams);
    }
}


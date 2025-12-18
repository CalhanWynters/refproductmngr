package com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Central repository for all domain-level constants related to weight management.
 */
final class WeightConstants {
    private WeightConstants() {} // Prevent instantiation

    // General Constraints
    public static final BigDecimal MAX_GRAMS = BigDecimal.valueOf(100000.0); // 100 kg agreed limit
    public static final BigDecimal MIN_GRAMS = BigDecimal.valueOf(0.001); // 1 milligram

    // Precision and Scaling
    public static final int COMPARISON_SCALE = 8; // Scale used for internal comparison in grams
    public static final int NORMALIZATION_SCALE = 4; // Scale for display/storage normalization
    public static final int INTERNAL_CALCULATION_SCALE = 8; // Scale for unit conversions

    // MathContext for internal high-precision multiplication/division
    public static final MathContext INTERNAL_MATH_CONTEXT = new MathContext(16, RoundingMode.HALF_UP);
}

package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static com.github.calhanwynters.refproductmngr.domain.product.WeightUnitEnums.*;

class WeightDomainTest {

    // Helper for creating BigDecimals easily in tests
    private BigDecimal bd(String value) {
        return new BigDecimal(value);
    }

    // --- WeightVO Validation Tests ---

    @Test
    @DisplayName("Should create WeightVO with valid positive amount")
    void shouldCreateValidWeightVO() {
        WeightVO weight = WeightVO.ofGrams(bd("50.0"));
        assertNotNull(weight);
        assertEquals(bd("50"), weight.amount()); // Normalization strips trailing zeros
        assertEquals(GRAM, weight.unit());
    }

    @Test
    @DisplayName("Should throw exception for negative amount")
    void shouldThrowForNegativeAmount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> WeightVO.ofGrams(bd("-10.0")));
        assertTrue(thrown.getMessage().contains("Amount must not be negative"));
    }

    @Test
    @DisplayName("Should throw exception for zero amount (due to MIN_GRAMS constraint in VO)")
    void shouldThrowForZeroAmount() {
        // The VO constructor validates against MIN_GRAMS > 0
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> WeightVO.ofGrams(BigDecimal.ZERO));
        assertTrue(thrown.getMessage().contains("Amount must be greater than 0.001g"));
    }

    @Test
    @DisplayName("Should throw exception for amount exceeding MAX_GRAMS")
    void shouldThrowForMaxAmountExceeded() {
        // MAX_GRAMS is 100000.0 (100kg)
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            WeightVO.ofKilograms(bd("100.01")); // Slightly over 100kg
        });
        assertTrue(thrown.getMessage().contains("Amount exceeds maximum allowed weight (100000g)"));
    }

    @Test
    @DisplayName("Should allow amount exactly at MAX_GRAMS limit")
    void shouldAllowMaxAmount() {
        WeightVO weight = WeightVO.ofKilograms(bd("100.0"));
        assertNotNull(weight);
        assertEquals(bd("100"), weight.amount());
    }

    // --- WeightVO Equality and Comparability Tests ---

    @Test
    @DisplayName("WeightVOs with same value and unit should be equal")
    void shouldBeEqual() {
        WeightVO w1 = WeightVO.ofGrams(bd("10.000"));
        WeightVO w2 = WeightVO.ofGrams(bd("10.0"));
        assertEquals(w1, w2);
        assertEquals(0, w1.compareTo(w2));
    }

    @Test
    @DisplayName("WeightVOs with different units but same weight should be comparable as equal")
    void shouldBeComparableAcrossUnits() {
        WeightVO w_kg = WeightVO.ofKilograms(bd("1.0")); // 1000g
        WeightVO w_g  = WeightVO.ofGrams(bd("1000.0")); // 1000g

        // The compareTo method uses grams internally and should return 0
        assertEquals(0, w_kg.compareTo(w_g));
        assertEquals(0, w_g.compareTo(w_kg));

        // Note: equals() uses component equality (unit and amount must match)
        assertNotEquals(w_kg, w_g);
    }

    // --- Unit Conversion Tests ---

    @Test
    @DisplayName("Enum conversions (KG to Grams and back) should be accurate")
    void testKilogramConversions() {
        BigDecimal valueInKg = bd("2.5");
        BigDecimal expectedGrams = bd("2500.0");

        assertEquals(expectedGrams, KILOGRAM.toGrams(valueInKg));

        BigDecimal backToKg = KILOGRAM.fromGrams(expectedGrams);
        // Uses normalization scale of 4 defined in constants
        assertEquals(bd("2.5"), backToKg);
    }

    @Test
    @DisplayName("Enum conversions (Pound to Grams and back) should be accurate with defined precision")
    void testPoundConversions() {
        BigDecimal onePound = bd("1.0");
        // 453.59237 g
        BigDecimal grams = POUND.toGrams(onePound);

        // Check conversion to grams using high precision internally
        assertEquals(bd("453.59237"), grams.stripTrailingZeros());

        // Check conversion back to pounds (should normalize to defined scale)
        BigDecimal backToPound = POUND.fromGrams(grams);
        // Because of the rounding in fromGrams(SCALE=8), it matches the original 1.0 exactly
        assertEquals(bd("1"), backToPound);
    }

    @Test
    @DisplayName("Convert between arbitrary units (Pounds to Carats) using convertTo method")
    void testArbitraryUnitConversion() {
        // 1 lb -> 453.59237 g -> (453.59237 / 0.2) carats = 2267.96185 carats
        WeightVO onePound = WeightVO.ofPounds(bd("1.0"));

        WeightVO carats = onePound.convertTo(CARAT);

        // The resulting amount should be normalized via the WeightVO constructor's normalize method (scale 4)
        // 2267.96185 rounds to 2267.9619
        BigDecimal expectedCarats = bd("2267.9619");

        assertEquals(CARAT, carats.unit());
        assertEquals(expectedCarats, carats.amount());
    }
}

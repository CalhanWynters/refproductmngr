package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.WeightVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static com.github.calhanwynters.refproductmngr.businesscore.domain.product.WeightUnitEnums.*;

class WeightDomainTest {

    // Helper for creating BigDecimals easily in tests
    private BigDecimal bd(String value) {
        return new BigDecimal(value);
    }

    // A helper method for asserting BigDecimal equality by magnitude (ignoring scale/representation)
    private void assertBigDecimalEquals(String expected, BigDecimal actual) {
        // Use compareTo for magnitude comparison, ensuring they are logically the same number
        assertEquals(0, bd(expected).compareTo(actual), "BigDecimal values should be equal in magnitude");
    }

    // --- WeightVO Validation Tests ---

    @Test
    @DisplayName("Should create WeightVO with valid positive amount")
    void shouldCreateValidWeightVO() {
        WeightVO weight = WeightVO.ofGrams(bd("50.0"));
        assertNotNull(weight);
        // Use the new helper method
        assertBigDecimalEquals("50", weight.amount());
        assertEquals(GRAM, weight.unit());
    }

    @Test
    @DisplayName("Should throw exception for negative amount")
    void shouldThrowForNegativeAmount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> WeightVO.ofGrams(bd("-10.0")));
        assertTrue(thrown.getMessage().contains("Amount must not be negative"));
    }

    @Test
    @DisplayName("Should allow zero amount (as the domain allows 0g weight)")
    void shouldAllowZeroAmount() {
        // The VO allows zero weight, so this test should pass without throwing an exception.
        assertDoesNotThrow(() -> WeightVO.ofGrams(BigDecimal.ZERO));
        WeightVO zeroWeight = WeightVO.ofGrams(BigDecimal.ZERO);
        assertBigDecimalEquals("0", zeroWeight.amount());
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
        // Use the new helper method to avoid scientific notation comparison issues
        assertBigDecimalEquals("100", weight.amount());
    }

    // --- WeightVO Equality and Comparability Tests ---

    @Test
    @DisplayName("WeightVOs with same value and unit should be equal")
    void shouldBeEqual() {
        WeightVO w1 = WeightVO.ofGrams(bd("10.000"));
        WeightVO w2 = WeightVO.ofGrams(bd("10.0"));
        // Records' equals() uses the internal stored BD values.
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

        // Use the helper method for magnitude comparison
        assertBigDecimalEquals("2500.0", KILOGRAM.toGrams(valueInKg));

        BigDecimal backToKg = KILOGRAM.fromGrams(bd("2500.0"));
        // Use the helper method
        assertBigDecimalEquals("2.5", backToKg);
    }

    @Test
    @DisplayName("Enum conversions (Pound to Grams and back) should be accurate with defined precision")
    void testPoundConversions() {
        BigDecimal onePound = bd("1.0");
        BigDecimal grams = POUND.toGrams(onePound);

        // Check conversion to grams using high precision internally
        assertBigDecimalEquals("453.59237", grams);

        // Check conversion back to pounds
        BigDecimal backToPound = POUND.fromGrams(grams);
        assertBigDecimalEquals("1", backToPound);
    }

    @Test
    @DisplayName("Convert between arbitrary units (Pounds to Carats) using convertTo method")
    void testArbitraryUnitConversion() {
        WeightVO onePound = WeightVO.ofPounds(bd("1.0"));
        WeightVO carats = onePound.convertTo(CARAT);

        // 2267.96185 rounds to 2267.9619 after VO normalization
        BigDecimal expectedCarats = bd("2267.9619");

        assertEquals(CARAT, carats.unit());
        assertBigDecimalEquals(expectedCarats.toPlainString(), carats.amount());
    }
}

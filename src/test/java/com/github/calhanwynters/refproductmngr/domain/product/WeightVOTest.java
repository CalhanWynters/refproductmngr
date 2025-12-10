package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for WeightVO to validate its functionality, including newly added Kilogram and Pound support.
 */
public class WeightVOTest {

    // Helper method for BigDecimal comparisons using the VO's defined scale and rounding mode
    private void assertEqualsVOValue(String message, String expectedValueString, BigDecimal actualValue) {
        BigDecimal expected = new BigDecimal(expectedValueString)
                .setScale(WeightVO.WeightUnit.SCALE, WeightVO.WeightUnit.ROUNDING_MODE)
                .stripTrailingZeros();

        assertEquals(expected, actualValue.stripTrailingZeros(), message);
    }

    @Test
    public void testCreationWithValidValues() {
        WeightVO weight = WeightVO.ofGrams(new BigDecimal("500.0"));
        assertNotNull(weight);
        assertEqualsVOValue("Amount not normalized correctly", "500.0", weight.amount());
        assertEquals(WeightVO.WeightUnit.GRAM, weight.unit());
    }

    @Test
    public void testCreationWithInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                        new WeightVO(new BigDecimal("-1"), WeightVO.WeightUnit.GRAM),
                "Amount must not be negative");
    }

    @Test
    public void testCreationWithNullAmount() {
        assertThrows(NullPointerException.class, () ->
                        new WeightVO(null, WeightVO.WeightUnit.GRAM),
                "Amount must not be null");
    }

    @Test
    public void testCreationWithNullUnit() {
        assertThrows(NullPointerException.class, () ->
                        new WeightVO(new BigDecimal("10"), null),
                "Unit must not be null");
    }

    @Test
    public void testMaxWeightValidation() {
        assertThrows(IllegalArgumentException.class, () ->
                        WeightVO.ofGrams(new BigDecimal("100000.1")),
                "Amount exceeds maximum allowed weight");
    }

    @Test
    public void testFactoryMethodsForNewUnits() {
        WeightVO kgWeight = WeightVO.ofKilograms(new BigDecimal("5.5"));
        assertEquals(WeightVO.WeightUnit.KILOGRAM, kgWeight.unit());
        assertEqualsVOValue("Kilogram factory method failed amount", "5.5", kgWeight.amount());

        WeightVO lbWeight = WeightVO.ofPounds(new BigDecimal("10.2"));
        assertEquals(WeightVO.WeightUnit.POUND, lbWeight.unit());
        assertEqualsVOValue("Pound factory method failed amount", "10.2", lbWeight.amount());
    }

    @Test
    public void testConversions() {
        WeightVO weightInOunces = WeightVO.ofOunces(new BigDecimal("1")); // 1 Ounce Avoirdupois
        assertEqualsVOValue("inGrams() conversion failed", "28.3495", weightInOunces.inGrams());

        WeightVO weightInGrams = weightInOunces.toUnit(WeightVO.WeightUnit.GRAM);
        assertEqualsVOValue("toUnit(GRAM) amount failed", "28.3495", weightInGrams.amount());

        WeightVO weightInCarats = weightInGrams.toUnit(WeightVO.WeightUnit.CARAT);
        assertEqualsVOValue("toUnit(CARAT) amount failed", "141.7475", weightInCarats.amount());

        WeightVO weightInTroyOunces = weightInGrams.toUnit(WeightVO.WeightUnit.TROY_OUNCE);
        assertEqualsVOValue("toUnit(TROY_OUNCE) amount failed", "0.9115", weightInTroyOunces.amount());

        WeightVO weightInKg = weightInGrams.toUnit(WeightVO.WeightUnit.KILOGRAM);
        assertEqualsVOValue("toUnit(KILOGRAM) amount failed", "0.0283", weightInKg.amount());

        WeightVO weightInLbs = weightInGrams.toUnit(WeightVO.WeightUnit.POUND);
        assertEqualsVOValue("toUnit(POUND) amount failed", "0.0625", weightInLbs.amount());
    }

    @Test
    public void testAddWeights() {
        WeightVO weightA = WeightVO.ofGrams(new BigDecimal("500.0"));
        WeightVO weightB = WeightVO.ofOunces(new BigDecimal("1"));
        WeightVO totalWeight = weightA.add(weightB);
        // Total grams: 500.0 + 28.3495... = 528.3495
        assertEqualsVOValue("Addition failed", "528.3495", totalWeight.inGrams());

        // Add a Kilogram
        WeightVO weightC = WeightVO.ofKilograms(new BigDecimal("1.0")); // 1000g
        WeightVO totalWeight2 = totalWeight.add(weightC);
        // Total grams: 528.3495 + 1000.0 = 1528.3495
        assertEqualsVOValue("Addition with KG failed", "1528.3495", totalWeight2.inGrams());
    }

    @Test
    public void testSubtractWeights() {
        WeightVO weightA = WeightVO.ofGrams(new BigDecimal("500.0"));
        WeightVO weightB = WeightVO.ofOunces(new BigDecimal("1"));

        WeightVO remainingWeight = weightA.subtract(weightB);
        // Remaining grams: 500.0 - 28.3495... = 471.6505
        assertEqualsVOValue("Subtraction failed", "471.6505", remainingWeight.inGrams());

        // Subtract a Pound (453.59237g, which rounds to 453.5924 at scale 4 in the VO logic)
        WeightVO weightC = WeightVO.ofPounds(new BigDecimal("1"));
        WeightVO remainingWeight2 = remainingWeight.subtract(weightC);
        // Remaining grams: 471.6505 - 453.5924 = 18.0581
        assertEqualsVOValue("Subtraction with LB failed", "18.0581", remainingWeight2.inGrams());

        // Test subtraction resulting in negative (should throw exception)
        WeightVO smallWeight = WeightVO.ofGrams(new BigDecimal("10"));
        assertThrows(IllegalArgumentException.class, () ->
                        smallWeight.subtract(weightB),
                "Resulting weight must not be negative");
    }

    @Test
    public void testComparison() {
        WeightVO weightA = WeightVO.ofGrams(new BigDecimal("500.0"));
        WeightVO weightB = WeightVO.ofOunces(new BigDecimal("1")); // ~28.35g

        assertTrue(weightA.compareTo(weightB) > 0, "Weight A should be greater than Weight B");
        assertTrue(weightB.compareTo(weightA) < 0, "Weight B should be less than Weight A");
        assertEquals(0, weightA.compareTo(WeightVO.ofGrams(new BigDecimal("500.0"))),
                "Equal weights should return 0");

        // Compare LBS and KGs
        WeightVO onePound = WeightVO.ofPounds(BigDecimal.ONE);
        WeightVO halfKilo = WeightVO.ofKilograms(new BigDecimal("0.5")); // 500g > ~453g
        assertTrue(halfKilo.compareTo(onePound) > 0, "0.5 KG should be greater than 1 LB");
    }
}




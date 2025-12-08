package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the WeightUnit enum to verify conversion logic and precision
 * across all supported units (Gram, Ounce, Troy Ounce, Carat, Kilogram, Pound).
 */
public class WeightUnitEnumsTest {

    // Helper method for BigDecimal comparisons using standard equals() after stripping trailing zeros,
    // to match the enum's 'fromGrams' behavior.
    private void assertEqualsBigDecimal(String message, BigDecimal expected, BigDecimal actual) {
        // We use the standard assertEquals on stripped values.
        assertEquals(expected.stripTrailingZeros(), actual.stripTrailingZeros(), message);
    }

    @Test
    public void testEnumConstantsExist() {
        assertNotNull(WeightUnitEnums.GRAM);
        assertNotNull(WeightUnitEnums.OUNCE);
        assertNotNull(WeightUnitEnums.CARAT);
        assertNotNull(WeightUnitEnums.TROY_OUNCE);
        // Verify new constants
        assertNotNull(WeightUnitEnums.KILOGRAM);
        assertNotNull(WeightUnitEnums.POUND);
    }

    @Test
    public void testGramConversions() {
        BigDecimal value = new BigDecimal("100.5");
        // GRAM to GRAM
        assertEqualsBigDecimal("GRAM to GRAM conversion failed", value, WeightUnitEnums.GRAM.convertValueTo(value, WeightUnitEnums.GRAM));

        // GRAM to OUNCE
        BigDecimal expectedOunce = new BigDecimal("3.54503318");
        BigDecimal actualOunce = WeightUnitEnums.GRAM.convertValueTo(value, WeightUnitEnums.OUNCE);
        assertEqualsBigDecimal("GRAM to OUNCE conversion failed", expectedOunce, actualOunce);

        // GRAM to CARAT
        BigDecimal expectedCarat = new BigDecimal("502.5");
        BigDecimal actualCarat = WeightUnitEnums.GRAM.convertValueTo(value, WeightUnitEnums.CARAT);
        assertEqualsBigDecimal("GRAM to CARAT conversion failed", expectedCarat, actualCarat);

        // GRAM to TROY_OUNCE
        BigDecimal expectedTroyOunce = new BigDecimal("3.23115003");
        BigDecimal actualTroyOunce = WeightUnitEnums.GRAM.convertValueTo(value, WeightUnitEnums.TROY_OUNCE);
        assertEqualsBigDecimal("GRAM to TROY_OUNCE conversion failed", expectedTroyOunce, actualTroyOunce);

        // GRAM to KILOGRAM
        BigDecimal expectedKilogram = new BigDecimal("0.1005");
        BigDecimal actualKilogram = WeightUnitEnums.GRAM.convertValueTo(value, WeightUnitEnums.KILOGRAM);
        assertEqualsBigDecimal("GRAM to KILOGRAM conversion failed", expectedKilogram, actualKilogram);

        // GRAM to POUND
        BigDecimal expectedPound = new BigDecimal("0.22156457");
        BigDecimal actualPound = WeightUnitEnums.GRAM.convertValueTo(value, WeightUnitEnums.POUND);
        assertEqualsBigDecimal("GRAM to POUND conversion failed", expectedPound, actualPound);
    }

    @Test
    public void testOunceConversions() {
        BigDecimal value = new BigDecimal("1");
        // OUNCE to GRAM
        BigDecimal expectedGram = new BigDecimal("28.349523125");
        assertEqualsBigDecimal("OUNCE to GRAM conversion failed", expectedGram, WeightUnitEnums.OUNCE.convertValueTo(value, WeightUnitEnums.GRAM));

        // OUNCE to CARAT
        BigDecimal expectedCarat = new BigDecimal("141.74761563");
        BigDecimal actualCarat = WeightUnitEnums.OUNCE.convertValueTo(value, WeightUnitEnums.CARAT);
        assertEqualsBigDecimal("OUNCE to CARAT conversion failed", expectedCarat, actualCarat);

        // OUNCE to TROY_OUNCE
        BigDecimal expectedTroyOunce = new BigDecimal("0.91145833");
        BigDecimal actualTroyOunce = WeightUnitEnums.OUNCE.convertValueTo(value, WeightUnitEnums.TROY_OUNCE);
        assertEqualsBigDecimal("OUNCE to TROY_OUNCE conversion failed", expectedTroyOunce, actualTroyOunce);
    }

    @Test
    public void testCaratConversions() {
        BigDecimal value = new BigDecimal("100");
        // CARAT to GRAM
        BigDecimal expectedGram = new BigDecimal("20.0");
        assertEqualsBigDecimal("CARAT to GRAM conversion failed", expectedGram, WeightUnitEnums.CARAT.convertValueTo(value, WeightUnitEnums.GRAM));

        // CARAT to OUNCE
        BigDecimal expectedOunce = new BigDecimal("0.70547924");
        BigDecimal actualOunce = WeightUnitEnums.CARAT.convertValueTo(value, WeightUnitEnums.OUNCE);
        assertEqualsBigDecimal("CARAT to OUNCE conversion failed", expectedOunce, actualOunce);

        // CARAT to TROY_OUNCE
        BigDecimal expectedTroyOunce = new BigDecimal("0.64301493");
        BigDecimal actualTroyOunce = WeightUnitEnums.CARAT.convertValueTo(value, WeightUnitEnums.TROY_OUNCE);
        assertEqualsBigDecimal("CARAT to TROY_OUNCE conversion failed", expectedTroyOunce, actualTroyOunce);
    }

    @Test
    public void testTroyOunceConversions() {
        BigDecimal value = new BigDecimal("1");
        // TROY_OUNCE to GRAM
        BigDecimal expectedGram = new BigDecimal("31.1034768");
        assertEqualsBigDecimal("TROY_OUNCE to GRAM conversion failed", expectedGram, WeightUnitEnums.TROY_OUNCE.convertValueTo(value, WeightUnitEnums.GRAM));

        // TROY_OUNCE to OUNCE
        BigDecimal expectedOunce = new BigDecimal("1.09714286");
        BigDecimal actualOunce = WeightUnitEnums.TROY_OUNCE.convertValueTo(value, WeightUnitEnums.OUNCE);
        assertEqualsBigDecimal("TROY_OUNCE to OUNCE conversion failed", expectedOunce, actualOunce);

        // TROY_OUNCE to CARAT
        BigDecimal expectedCarat = new BigDecimal("155.517384");
        BigDecimal actualCarat = WeightUnitEnums.TROY_OUNCE.convertValueTo(value, WeightUnitEnums.CARAT);
        assertEqualsBigDecimal("TROY_OUNCE to CARAT conversion failed", expectedCarat, actualCarat);
    }

    @Test
    public void testKilogramConversions() {
        BigDecimal value = new BigDecimal("1.5");
        // KILOGRAM to GRAM
        BigDecimal expectedGram = new BigDecimal("1500.0");
        assertEqualsBigDecimal("KILOGRAM to GRAM conversion failed", expectedGram, WeightUnitEnums.KILOGRAM.convertValueTo(value, WeightUnitEnums.GRAM));

        // KILOGRAM to POUND
        BigDecimal expectedPound = new BigDecimal("3.30693393");
        BigDecimal actualPound = WeightUnitEnums.KILOGRAM.convertValueTo(value, WeightUnitEnums.POUND);
        assertEqualsBigDecimal("KILOGRAM to POUND conversion failed", expectedPound, actualPound);

        // KILOGRAM to OUNCE (Corrected Expected Value)
        // 1500g / 28.349523125 g/oz = 52.91094292
        BigDecimal expectedOunce = new BigDecimal("52.91094292");
        BigDecimal actualOunce = WeightUnitEnums.KILOGRAM.convertValueTo(value, WeightUnitEnums.OUNCE);
        assertEqualsBigDecimal("KILOGRAM to OUNCE conversion failed", expectedOunce, actualOunce);
    }

    @Test
    public void testPoundConversions() {
        BigDecimal value = new BigDecimal("2");
        // POUND to GRAM (2 lbs = ~907.18474 g)
        BigDecimal expectedGram = new BigDecimal("907.18474");
        assertEqualsBigDecimal("POUND to GRAM conversion failed", expectedGram, WeightUnitEnums.POUND.convertValueTo(value, WeightUnitEnums.GRAM));

        // POUND to KILOGRAM (2 lbs = ~0.90718 kg)
        BigDecimal expectedKilogram = new BigDecimal("0.90718474");
        BigDecimal actualKilogram = WeightUnitEnums.POUND.convertValueTo(value, WeightUnitEnums.KILOGRAM);
        assertEqualsBigDecimal("POUND to KILOGRAM conversion failed", expectedKilogram, actualKilogram);

        // POUND to OUNCE (2 lbs = 32 oz)
        BigDecimal expectedOunce = new BigDecimal("32.0");
        BigDecimal actualOunce = WeightUnitEnums.POUND.convertValueTo(value, WeightUnitEnums.OUNCE);
        assertEqualsBigDecimal("POUND to OUNCE conversion failed", expectedOunce, actualOunce);
    }

    @Test
    public void testZeroValueConversions() {
        BigDecimal zero = BigDecimal.ZERO;
        for (WeightUnitEnums source : WeightUnitEnums.values()) {
            for (WeightUnitEnums target : WeightUnitEnums.values()) {
                BigDecimal result = source.convertValueTo(zero, target);
                assertEqualsBigDecimal(source + " to " + target + " zero conversion failed", BigDecimal.ZERO, result);
            }
        }
    }

    @Test
    public void testNegativeValueConversions() {
        BigDecimal negativeValue = new BigDecimal("-1");

        for (WeightUnitEnums source : WeightUnitEnums.values()) {
            for (WeightUnitEnums target : WeightUnitEnums.values()) {
                // Use assertThrows to check for IllegalArgumentException
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> source.convertValueTo(negativeValue, target));
                assertEquals("Value must not be negative", exception.getMessage());
            }
        }
    }
}

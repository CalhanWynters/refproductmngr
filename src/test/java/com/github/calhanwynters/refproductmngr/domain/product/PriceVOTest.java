package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class PriceVOTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency EUR = Currency.getInstance("EUR");

    @Test
    void constructor_shouldThrowException_whenValueIsNull() {
        // Test primary constructor behavior when value is null
        assertThrows(IllegalArgumentException.class, () -> new PriceVO(null, 2, USD),
                "Price must be non-negative");
    }

    @Test
    void constructor_shouldThrowException_whenValueIsNegative() {
        // Test primary constructor behavior when value is negative
        assertThrows(IllegalArgumentException.class, () -> new PriceVO(new BigDecimal("-10.00"), 2, USD),
                "Price must be non-negative");
    }

    @Test
    void constructor_shouldThrowException_whenPrecisionIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new PriceVO(new BigDecimal("10.00"), -1, USD),
                "Precision must be non-negative");
    }

    @Test
    void constructor_shouldThrowException_whenCurrencyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new PriceVO(new BigDecimal("10.00"), 2, null),
                "Currency must not be null");
    }

    @Test
    void constructor_secondary_shouldSetDefaultValues() {
        // Secondary constructor defaults to USD and precision 2
        PriceVO price = new PriceVO(new BigDecimal("10.4567"));
        assertEquals(2, price.precision());
        assertEquals(USD, price.currency());

        // The value should be stored exactly as passed, but scaled when accessed/used.
        // We verify the internal value is correct, and the scaled value matches expectations.
        assertEquals(0, new BigDecimal("10.4567").compareTo(price.value()));
        assertEquals(new BigDecimal("10.46"), price.value().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void constructor_primary_shouldSetAllSpecifiedValues() {
        PriceVO price = new PriceVO(new BigDecimal("10.4567"), 4, EUR);
        assertEquals(4, price.precision());
        assertEquals(EUR, price.currency());
        assertEquals(new BigDecimal("10.4567"), price.value().setScale(4, RoundingMode.HALF_UP));
    }

    @Test
    void toString_shouldReturnFormattedString_withUSD() {
        // Assuming default locale uses "$" for USD
        PriceVO price = new PriceVO(new BigDecimal("10.4567"), 2, USD);
        String expectedSymbol = USD.getSymbol();
        assertEquals(String.format("%s 10.46", expectedSymbol), price.toString());
    }

    @Test
    void toString_shouldReturnFormattedString_withEUR() {
        // Assuming default locale uses "€" for EUR (may vary by system locale)
        PriceVO price = new PriceVO(new BigDecimal("100"), 0, EUR);
        String expectedSymbol = EUR.getSymbol();
        assertEquals(String.format("%s 100", expectedSymbol), price.toString());
    }

    @Test
    void equals_shouldReturnTrue_whenAllComponentsAreEqual() {
        PriceVO price1 = new PriceVO(new BigDecimal("10.50"), 2, USD);
        PriceVO price2 = new PriceVO(new BigDecimal("10.50"), 2, USD);
        assertEquals(price1, price2);
    }

    @Test
    void equals_shouldReturnFalse_whenValuesAreDifferent() {
        PriceVO price1 = new PriceVO(new BigDecimal("10.50"), 2, USD);
        PriceVO price2 = new PriceVO(new BigDecimal("20.00"), 2, USD);
        assertNotEquals(price1, price2);
    }

    @Test
    void equals_shouldReturnFalse_whenCurrenciesAreDifferent() {
        PriceVO price1 = new PriceVO(new BigDecimal("10.50"), 2, USD);
        PriceVO price2 = new PriceVO(new BigDecimal("10.50"), 2, EUR);
        assertNotEquals(price1, price2);
    }

    @Test
    void equals_shouldReturnFalse_whenPrecisionsAreDifferent() {
        PriceVO price1 = new PriceVO(new BigDecimal("10.50"), 2, USD);
        PriceVO price2 = new PriceVO(new BigDecimal("10.50"), 3, USD);
        assertNotEquals(price1, price2);
    }


    @Test
    void hashCode_shouldBeConsistent_forEqualObjects() {
        PriceVO price1 = new PriceVO(new BigDecimal("10.50"), 2, USD);
        PriceVO price2 = new PriceVO(new BigDecimal("10.50"), 2, USD);
        assertEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    void hashCode_shouldNotBeEqualForDifferentObjects() {
        PriceVO price1 = new PriceVO(new BigDecimal("10.50"), 2, USD);
        PriceVO price2 = new PriceVO(new BigDecimal("20.00"), 2, USD);
        assertNotEquals(price1.hashCode(), price2.hashCode());
    }
}

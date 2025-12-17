package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.PriceVO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class PriceVOTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency EUR = Currency.getInstance("EUR");

    // Note: For constructors that accept an expected message,
    // it's generally cleaner to capture the exception and assert the message
    // explicitly, as shown in previous examples (like NameVOTest).
    // The current style works, but the explicit assert is often preferred.

    @Test
    void constructor_shouldThrowException_whenValueIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new PriceVO(null),
                "Price cannot be null");
    }

    @Test
    void constructor_shouldThrowException_whenValueIsNegative() {
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
        PriceVO price = new PriceVO(new BigDecimal("10.4567"));
        assertEquals(2, price.precision());
        assertEquals(USD, price.currency());

        // PriceVO stores the raw input value internally
        assertEquals(new BigDecimal("10.4567"), price.value());
    }

    @Test
    void constructor_primary_shouldSetAllSpecifiedValues() {
        PriceVO price = new PriceVO(new BigDecimal("10.4567"), 4, EUR);
        assertEquals(4, price.precision());
        assertEquals(EUR, price.currency());
        assertEquals(new BigDecimal("10.4567"), price.value());
    }

    @Test
    void toString_shouldReturnFormattedString_withUSD() {
        // Input value is 10.4567, but precision is 2, so it formats to 10.46
        PriceVO price = new PriceVO(new BigDecimal("10.4567"), 2, USD);
        assertEquals(String.format("%s 10.46", USD.getSymbol()), price.toString());
    }

    @Test
    void toString_shouldReturnFormattedString_withEUR() {
        PriceVO price = new PriceVO(new BigDecimal("100"), 0, EUR);
        assertEquals(String.format("%s 100", EUR.getSymbol()), price.toString());
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
        // Although the displayed value might be the same, the VOs represent different precisions
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

    @Test
    void constructor_shouldHandleLargeValues() {
        PriceVO price = new PriceVO(new BigDecimal("1000000.1234"), 2, USD);
        // We assert the raw internal value (stored as input) and rely on toString() tests for formatting
        assertEquals(new BigDecimal("1000000.1234"), price.value());
    }

    @Test
    void toString_shouldHandleLargeValues() {
        // Input 1000000.1234 formats to 1000000.12 with precision 2
        PriceVO price = new PriceVO(new BigDecimal("1000000.1234"), 2, USD);
        assertEquals(String.format("%s 1000000.12", USD.getSymbol()), price.toString());
    }
}

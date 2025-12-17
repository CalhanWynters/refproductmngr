package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * An immutable Value Object representing a monetary price.
 * Ensures non-negative values and handles display precision.
 */
public record PriceVO(BigDecimal value, int precision, Currency currency) {

    public PriceVO(BigDecimal value) {
        this(value, 2, Currency.getInstance("USD")); // Defaults to 2 decimal points and USD if not specified
    }

    public PriceVO {
        if (value == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        if (precision < 0) {
            throw new IllegalArgumentException("Precision must be non-negative");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }
    }

    @Override
    public String toString() {
        BigDecimal scaledValue = value.setScale(precision, RoundingMode.HALF_UP);
        return String.format("%s %s", currency.getSymbol(), scaledValue.toPlainString());
    }
}

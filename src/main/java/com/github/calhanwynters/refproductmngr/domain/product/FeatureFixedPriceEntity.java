package com.github.calhanwynters.refproductmngr.domain.product;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A feature that adds a fixed amount to the base price (e.g., gift wrapping service).
 * Implemented as an immutable entity, using Value Objects for name/label and BigDecimal for price accuracy.
 */
public class FeatureFixedPriceEntity extends FeatureAbstractClass {
    // Use BigDecimal for financial accuracy and make the field final
    private final BigDecimal fixedPrice;

    public FeatureFixedPriceEntity(
            FeatureIdVO id,
            NameVO nameVO,          // Changed from String name
            DescriptionVO description,
            LabelVO labelVO,        // Changed from String label
            BigDecimal fixedPrice   // Changed from double fixedPrice
    ) {
        // Pass the new Value Objects up to the abstract superclass constructor
        super(id, nameVO, description, labelVO);

        Objects.requireNonNull(fixedPrice, "Fixed price must not be null.");

        if (fixedPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Fixed price cannot be negative.");
        }
        this.fixedPrice = fixedPrice;
    }

    // Public Getter for fixedPrice
    public BigDecimal getFixedPrice() {
        return fixedPrice;
    }

    // You could also implement an abstract method from FeatureAbstractClass here, e.g.:
    /*
    @Override
    public BigDecimal calculatePriceEffect() {
        return this.fixedPrice;
    }
    */
}

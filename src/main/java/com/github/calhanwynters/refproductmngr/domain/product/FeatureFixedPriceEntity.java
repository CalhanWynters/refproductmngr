package com.github.calhanwynters.refproductmngr.domain.product;

import java.math.BigDecimal;

/**
 * A feature that adds a fixed amount to the base price (e.g., gift wrapping service).
 * Implemented as an immutable entity, using Value Objects for name/label and BigDecimal for price accuracy.
 */
public class FeatureFixedPriceEntity extends FeatureAbstractClass {
    // Use BigDecimal for financial accuracy and make the field final
    private final BigDecimal fixedPrice;

    public FeatureFixedPriceEntity(
            FeatureIdVO id,
            NameVO nameVO,
            DescriptionVO description,
            LabelVO labelVO,
            BigDecimal fixedPrice
    ) {
        super(id, nameVO, description, labelVO);

        // Check for null here
        if (fixedPrice == null) {
            throw new IllegalArgumentException("Fixed price must not be null.");
        }

        if (fixedPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Fixed price cannot be negative.");
        }
        this.fixedPrice = fixedPrice;
    }

    // Public Getter for fixedPrice
    public BigDecimal getFixedPrice() {
        return fixedPrice;
    }

}

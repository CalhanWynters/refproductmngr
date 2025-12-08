package com.github.calhanwynters.refproductmngr.domain.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * A feature where price scales based on quantity/measurement (e.g., custom length of fabric).
 * Updated to use BigDecimal for precise currency representation and calculation methods,
 * and NameVO/LabelVO for enhanced encapsulation.
 */
public class FeatureScalingPriceEntity extends FeatureAbstractClass {

    private final String measurementUnit;
    private final BigDecimal baseAmount;
    private final BigDecimal incrementAmount;
    private final int maxQuantity;

    public FeatureScalingPriceEntity(
            FeatureIdVO id,
            NameVO nameVO,          // Changed from String name
            DescriptionVO description,
            LabelVO labelVO,        // Changed from String label
            String measurementUnit,
            BigDecimal baseAmount,
            BigDecimal incrementAmount,
            int maxQuantity
    ) {
        // Pass the new Value Objects up to the abstract superclass constructor
        super(id, nameVO, description, labelVO);

        this.measurementUnit = Objects.requireNonNull(measurementUnit, "Measurement unit must not be null");
        this.baseAmount = Objects.requireNonNull(baseAmount, "Base amount must not be null");
        this.incrementAmount = Objects.requireNonNull(incrementAmount, "Increment amount must not be null");

        if (baseAmount.compareTo(BigDecimal.ZERO) < 0 || incrementAmount.compareTo(BigDecimal.ZERO) < 0 || maxQuantity < 0) {
            throw new IllegalArgumentException("Amounts and max quantity must be non-negative.");
        }

        this.maxQuantity = maxQuantity;
    }

    // --- Getters for scaling parameters ---

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public BigDecimal getIncrementAmount() {
        return incrementAmount;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    // --- Business Logic ---

    public BigDecimal calculateTotalPrice(int quantityRequested) {
        if (quantityRequested <= 0 || quantityRequested > maxQuantity) {
            throw new IllegalArgumentException(
                    "Quantity must be between 1 and " + maxQuantity + ", but was " + quantityRequested
            );
        }

        BigDecimal quantityBD = BigDecimal.valueOf(quantityRequested);
        BigDecimal incrementTotal = this.incrementAmount.multiply(quantityBD);
        BigDecimal total = this.baseAmount.add(incrementTotal);

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    // --- Object Overrides (equals/hashCode/toString) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FeatureScalingPriceEntity that = (FeatureScalingPriceEntity) o;
        return maxQuantity == that.maxQuantity &&
                Objects.equals(measurementUnit, that.measurementUnit) &&
                Objects.equals(baseAmount, that.baseAmount) &&
                Objects.equals(incrementAmount, that.incrementAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), measurementUnit, baseAmount, incrementAmount, maxQuantity);
    }

    @Override
    public String toString() {
        return "FeatureScalingPriceEntity{" +
                "id=" + getId() +
                ", name='" + getNameVO().value() + '\'' + // Use the NameVO getter correctly
                ", unit='" + measurementUnit + '\'' +
                ", base=" + baseAmount +
                ", increment=" + incrementAmount +
                ", max=" + maxQuantity +
                '}';
    }
}

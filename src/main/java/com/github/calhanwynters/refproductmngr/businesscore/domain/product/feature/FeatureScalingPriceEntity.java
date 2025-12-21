package com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * A feature where price scales based on quantity/measurement (e.g., custom length of fabric).
 */
public class FeatureScalingPriceEntity extends FeatureAbstractClass {

    private final MeasurementUnitVO measurementUnit;
    private final BigDecimal baseAmount;
    private final BigDecimal incrementAmount;
    private final int maxQuantity;

    public FeatureScalingPriceEntity(
            FeatureIdVO id,
            NameVO nameVO,
            DescriptionVO description,
            LabelVO labelVO,
            MeasurementUnitVO measurementUnit, // Do not confuse with currency. This deals with metric or imperial units.
            BigDecimal baseAmount,  // starting amount at 0 units
            BigDecimal incrementAmount, // establishes price (without currency indicator) increase per unit.
            int maxQuantity, // establishes maximum quantity limit.
            Boolean isUnique
    ) {
        super(id, nameVO, labelVO, description, isUnique);

        // Validation checks
        if (measurementUnit == null) {
            throw new IllegalArgumentException("Measurement unit must not be null.");
        }
        if (baseAmount == null) {
            throw new IllegalArgumentException("Base amount must not be null.");
        }
        if (incrementAmount == null) {
            throw new IllegalArgumentException("Increment amount must not be null.");
        }
        if (baseAmount.compareTo(BigDecimal.ZERO) < 0 || incrementAmount.compareTo(BigDecimal.ZERO) < 0 || maxQuantity < 0) {
            throw new IllegalArgumentException("Amounts and max quantity must be non-negative.");
        }

        this.measurementUnit = measurementUnit;
        this.baseAmount = baseAmount;
        this.incrementAmount = incrementAmount;
        this.maxQuantity = maxQuantity;
    }

    // --- Getters for scaling parameters ---

    public MeasurementUnitVO getMeasurementUnit() {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        // Note: calling super.equals(o) here uses only the ID for equality comparison,
        // which might conflict with including other fields below. For a domain entity,
        // ID-based equality (from FeatureAbstractClass) is often correct.
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
                // FIX: Changed name() to value()
                ", name='" + getNameVO().value() + '\'' +
                // FIX: Changed getUnit() to unit() (from previous correction)
                ", unit='" + measurementUnit.unit() + '\'' +
                ", base=" + baseAmount +
                ", increment=" + incrementAmount +
                ", max=" + maxQuantity +
                '}';
    }
}

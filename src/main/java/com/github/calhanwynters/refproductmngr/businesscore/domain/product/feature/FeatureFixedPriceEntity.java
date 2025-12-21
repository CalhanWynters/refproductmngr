package com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FeatureFixedPriceEntity extends FeatureAbstractClass {
    private final BigDecimal fixedPrice;

    public FeatureFixedPriceEntity(
            FeatureIdVO id,
            NameVO nameVO,
            DescriptionVO description,
            LabelVO labelVO,
            BigDecimal fixedPrice,
            Boolean isUnique
    ) {
        super(id, nameVO, labelVO, description, isUnique);

        if (fixedPrice == null) {
            throw new IllegalArgumentException("Fixed price must not be null.");
        }

        if (fixedPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Fixed price cannot be negative.");
        }

        // Normalize to 2 decimal places for consistent currency handling
        this.fixedPrice = fixedPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getFixedPrice() {
        return fixedPrice;
    }
}

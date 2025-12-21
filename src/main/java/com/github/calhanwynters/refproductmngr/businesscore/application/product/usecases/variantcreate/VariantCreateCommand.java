package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantcreate;

import java.math.BigDecimal;
import java.util.Set;

public record VariantCreateCommand(
        String sku,
        BigDecimal basePrice,
        BigDecimal currentPrice,
        String currencyCode,
        double weightValue,
        String weightUnit,
        String careInstructions,
        String status
) {}


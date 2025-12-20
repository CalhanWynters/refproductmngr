package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantcreate;

import java.math.BigDecimal;
import java.util.Set;

public record VariantCreateCommand(
        String productId, // Which aggregate does this belong to?
        String sku,
        BigDecimal price,
        String careInstructions,
        double weight
) {}

package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.productcreate;

import java.math.BigDecimal;
import java.util.List;

public record ProductCreateCommand(
        String businessId,
        String category,
        String description,
        List<String> imageUrls,
        String initialVariantSku,
        BigDecimal initialPrice,
        String currencyCode
) {}

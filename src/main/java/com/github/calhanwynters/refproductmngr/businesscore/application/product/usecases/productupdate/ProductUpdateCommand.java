package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.productupdate;

public record ProductUpdateCommand(
        String productId,
        String description,
        String categoryId
) {}

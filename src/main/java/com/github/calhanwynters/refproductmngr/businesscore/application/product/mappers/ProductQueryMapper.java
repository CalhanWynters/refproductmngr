package com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.ProductQueryDTO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregate;

public class ProductQueryMapper {
    private ProductQueryMapper() {}

    public static ProductQueryDTO toDTO(ProductAggregate aggregate) {
        if (aggregate == null) return null;

        return new ProductQueryDTO(
                aggregate.id().value(),
                aggregate.businessIdVO().value()
        );
    }
}

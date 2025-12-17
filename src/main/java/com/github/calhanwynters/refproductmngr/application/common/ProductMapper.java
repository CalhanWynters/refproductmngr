package com.github.calhanwynters.refproductmngr.application.common;

import com.github.calhanwynters.refproductmngr.domain.product.ImageUrlVO;
import com.github.calhanwynters.refproductmngr.domain.product.ProductAggregate;

import java.util.stream.Collectors;


public final class ProductMapper {

    private ProductMapper() {}

    /**
     * Maps ProductAggregate to ProductDTO for the Application Layer.
     */
    public static ProductDTO toDTO(ProductAggregate aggregate) {
        if (aggregate == null) return null;

        return new ProductDTO(
                aggregate.id().value(),
                aggregate.businessIdVO().value(),
                aggregate.category().value(),
                aggregate.description().description(),
                // Extracting String URLs from List<ImageUrlVO>
                aggregate.gallery().images().stream()
                        .map(ImageUrlVO::url)
                        .toList(),
                aggregate.variants().stream()
                        .map(VariantMapper::toDTO)
                        .collect(Collectors.toSet()),
                aggregate.version().version(),
                aggregate.isPublishable()
        );
    }
}
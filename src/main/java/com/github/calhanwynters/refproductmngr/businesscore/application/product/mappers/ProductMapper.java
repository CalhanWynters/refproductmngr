package com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.ProductDTO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ImageUrlVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregate;
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
                aggregate.description().text(),
                // Maps List<ImageUrlVO> to List<String>
                aggregate.gallery().images().stream().map(ImageUrlVO::url).toList(),
                // Maps Set<VariantEntity> to Set<VariantDTO>
                aggregate.variants().stream().map(VariantMapper::toDTO).collect(Collectors.toSet()),
                aggregate.version().num(),
                aggregate.isDeleted(), // Map the isDeleted field from Aggregate
                aggregate.isPublishable()
        );
    }
}

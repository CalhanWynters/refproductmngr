package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Collections;
import java.util.Set;

/**
 * Factory class for creating instances of ProductAggregate.
 */
public class ProductAggregateFactory {
    public static ProductAggregate create(
            BusinessIdVO businessIdVO,
            CategoryVO category,
            DescriptionVO description,
            GalleryVO gallery,
            VersionVO version,
            Set<VariantEntity> initialVariants
    ) {
        return new ProductAggregate(
                ProductIdVO.generate(),
                businessIdVO,
                category,
                description,
                gallery,
                initialVariants,
                version
        );
    }

    public static ProductAggregate create(
            BusinessIdVO businessIdVO,
            CategoryVO category,
            DescriptionVO description,
            GalleryVO gallery,
            VersionVO version
    ) {
        return create(businessIdVO, category, description, gallery, version, Collections.emptySet());
    }
}

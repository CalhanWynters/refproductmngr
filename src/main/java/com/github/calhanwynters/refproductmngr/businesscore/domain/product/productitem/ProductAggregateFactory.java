package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Full-Service Factory for ProductAggregate in 2025.
 * Coordinates creation across the deep graph: Features -> Variants -> Product.
 */
public class ProductAggregateFactory {

    // --- FEATURE CREATION ---

    public static FeatureBasicEntity createBasicFeature(
            NameVO name, LabelVO label, DescriptionVO desc, boolean isUnique) {
        return new FeatureBasicEntity(FeatureIdVO.generate(), name, desc, label, isUnique);
    }

    public static FeatureFixedPriceEntity createFixedPriceFeature(
            NameVO name, LabelVO label, DescriptionVO desc, BigDecimal price, boolean isUnique) {
        return new FeatureFixedPriceEntity(FeatureIdVO.generate(), name, desc, label, price, isUnique);
    }

    public static FeatureScalingPriceEntity createScalingPriceFeature(
            NameVO name, LabelVO label, DescriptionVO desc, MeasurementUnitVO unit,
            BigDecimal base, BigDecimal increment, int max, boolean isUnique) {
        return new FeatureScalingPriceEntity(
                FeatureIdVO.generate(), name, desc, label, unit, base, increment, max, isUnique);
    }

    // --- VARIANT CREATION ---

    public static VariantEntity createVariant(
            SkuVO sku, PriceVO basePrice, PriceVO currentPrice,
            Set<FeatureAbstractClass> features, CareInstructionVO care,
            WeightVO weight, VariantStatusEnums status) {

        // Factory provides the ID and ensures features set isn't null
        return new VariantEntity(
                VariantIdVO.generate(),
                sku, basePrice, currentPrice,
                features != null ? features : Set.of(),
                care, weight, status
        );
    }

    // --- AGGREGATE CREATION ---

    /**
     * Creates a NEW ProductAggregate.
     * Enforces the mandatory initial variant invariant.
     */
    public static ProductAggregate create(
            BusinessIdVO businessIdVO,
            CategoryVO category,
            DescriptionVO description,
            GalleryVO gallery,
            Set<VariantEntity> initialVariants
    ) {
        if (initialVariants == null || initialVariants.isEmpty()) {
            throw new IllegalArgumentException("A product must have at least one variant.");
        }

        return new ProductAggregate(
                ProductIdVO.generate(),
                businessIdVO,
                category,
                description,
                gallery,
                initialVariants,
                new VersionVO(1), // Starting business version, NOT a DB lock
                false
        );
    }

    /**
     * Reconstructs an existing ProductAggregate (Bypasses ID generation and default versioning).
     */
    public static ProductAggregate reconstruct(
            ProductIdVO id, BusinessIdVO businessId, CategoryVO category,
            DescriptionVO desc, GalleryVO gallery, Set<VariantEntity> variants,
            VersionVO version, boolean isDeleted) {
        return new ProductAggregate(id, businessId, category, desc, gallery, variants, version, isDeleted);
    }
}
